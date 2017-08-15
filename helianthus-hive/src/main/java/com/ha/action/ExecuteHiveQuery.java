package com.ha.action;

import com.ha.HiveAction;
import com.ha.HiveQueryExecutor;
import com.ha.exception.HiveQueryExecutionException;
import com.ha.exception.HiveViaHelianthusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;

/**
 * User: shuiqing
 * DateTime: 17/8/14 下午5:09
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class ExecuteHiveQuery implements HiveAction {

    private final static Logger LOG = LoggerFactory
            .getLogger(ExecuteHiveQuery.class);
    @HelianthusJobPropertyDescription("Verbatim query to execute. Can also specify hive.query.nn where nn is a series of padded numbers, which will be executed in order")
    public static final String HIVE_QUERY = "hive.query";
    @HelianthusJobPropertyDescription("File to load query from.  Should be in same zip.")
    public static final String HIVE_QUERY_FILE = "hive.query.file";
    @HelianthusJobPropertyDescription("URL to retrieve the query from.")
    public static final String HIVE_QUERY_URL = "hive.query.url";

    private final HiveQueryExecutor hqe;
    private final String q;

    public ExecuteHiveQuery(Properties properties, HiveQueryExecutor hqe)
            throws HiveViaHelianthusException {
        String singleLine = properties.getProperty(HIVE_QUERY);
        String multiLine = extractMultilineQuery(properties);
        String queryFile = extractQueryFromFile(properties);
        String queryURL = extractQueryFromURL(properties);

        this.q = determineQuery(singleLine, multiLine, queryFile, queryURL);
        this.hqe = hqe;
    }

    private String extractQueryFromFile(Properties properties)
            throws HiveViaHelianthusException {
        String file = properties.getProperty(HIVE_QUERY_FILE);

        if (file == null)
            return null;

        LOG.info("Attempting to read query from file: " + file);

        StringBuilder contents = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));

            String line;

            while ((line = br.readLine()) != null) {
                contents.append(line);
                contents.append(System.getProperty("line.separator"));
            }

        } catch (IOException e) {
            throw new HiveViaHelianthusException(e);
        } finally {
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    // TODO: Just throw IOException and catch-wrap in the constructor...
                    throw new HiveViaHelianthusException(e);
                }
        }

        return contents.toString();
    }

    private String extractQueryFromURL(Properties properties)
            throws HiveViaHelianthusException {
        String url = properties.getProperty(HIVE_QUERY_URL);

        if (url == null)
            return null;

        LOG.info("Attempting to retrieve query from URL: " + url);

        StringBuilder contents = new StringBuilder();
        BufferedReader br = null;

        try {
            URL queryURL = new URL(url);

            br = new BufferedReader(new InputStreamReader(queryURL.openStream()));
            String line;

            while ((line = br.readLine()) != null) {
                contents.append(line);
                contents.append(System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            throw new HiveViaHelianthusException(e);
        } finally {
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    // TODO: Just throw IOException and catch-wrap in the constructor...
                    throw new HiveViaHelianthusException(e);
                }
        }

        return contents.toString();
    }

    private String determineQuery(String singleLine, String multiLine,
                                  String queryFromFile, String queryFromURL) throws HiveViaHelianthusException {
        int specifiedValues = 0;

        for (String s : new String[] { singleLine, multiLine, queryFromFile,
                queryFromURL }) {
            if (s != null)
                specifiedValues++;
        }

        if (specifiedValues == 0)
            throw new HiveViaHelianthusException("Must specify " + HIVE_QUERY + " xor "
                    + HIVE_QUERY + ".nn xor " + HIVE_QUERY_FILE + " xor "
                    + HIVE_QUERY_URL + " in properties. Exiting.");

        if (specifiedValues != 1)
            throw new HiveViaHelianthusException("Must specify only " + HIVE_QUERY
                    + " or " + HIVE_QUERY + ".nn or " + HIVE_QUERY_FILE + " or "
                    + HIVE_QUERY_URL + " in properties, not more than one. Exiting.");

        if (singleLine != null) {
            LOG.info("Returning " + HIVE_QUERY + " = " + singleLine);
            return singleLine;
        } else if (multiLine != null) {
            LOG.info("Returning consolidated " + HIVE_QUERY + ".nn = " + multiLine);
            return multiLine;
        } else if (queryFromFile != null) {
            LOG.info("Returning query from file " + queryFromFile);
            return queryFromFile;
        } else {
            LOG.info("Returning query from URL " + queryFromURL);
            return queryFromURL;
        }
    }

    private String extractMultilineQuery(Properties properties) {
        ArrayList<String> lines = new ArrayList<String>();

        for (int i = 0; i < 100; i++) {
            String padded = String.format("%02d", i);
            String value = properties.getProperty(HIVE_QUERY + "." + padded);
            if (value != null) {
                lines.add(value);
            }
        }

        return HiveActionUtils.joinNewlines(lines);
    }

    @Override
    public void execute() throws HiveViaHelianthusException {
        try {
            hqe.executeQuery(q);
        } catch (HiveQueryExecutionException e) {
            throw new HiveViaHelianthusException(e);
        }
    }
}
