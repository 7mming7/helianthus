package com.ha.action;

import com.ha.exception.HiveViaHelianthusException;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

/**
 * User: shuiqing
 * DateTime: 17/8/14 下午5:06
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class HiveActionUtils {

    private final static Logger LOG = LoggerFactory.getLogger(HiveActionUtils.class);

    static ArrayList<String> fetchDirectories(FileSystem fs, String location,
                                              boolean returnFullPath) throws IOException, HiveViaHelianthusException {
        LOG.info("Fetching directories in " + location);
        Path p = new Path(location);
        FileStatus[] statuses = fs.listStatus(p);

        if (statuses == null || statuses.length == 0) {
            throw new HiveViaHelianthusException("Couldn't find any directories in "
                    + location);
        }

        ArrayList<String> files = new ArrayList<String>(statuses.length);
        for (FileStatus status : statuses) {
            if (!status.isDir())
                continue;
            if (status.getPath().getName().startsWith("."))
                continue;

            files.add(returnFullPath ? status.getPath().toString() : status.getPath()
                    .getName());
        }
        return files;
    }

    public static String joinNewlines(Collection<String> strings) {
        if (strings == null || strings.size() == 0)
            return null;

        StringBuilder sb = new StringBuilder();

        for (String s : strings) {
            String trimmed = s.trim();
            sb.append(trimmed);
            if (!trimmed.endsWith("\n"))
                sb.append("\n");
        }

        return sb.toString();
    }

    // Hey, look! It's this method again! It's the freaking Where's Waldo of
    // methods...
    public static String verifyProperty(Properties p, String key)
            throws HiveViaHelianthusException {
        String value = p.getProperty(key);
        if (value == null) {
            throw new HiveViaHelianthusException("Can't find property " + key
                    + " in provided Properties. Bailing");
        }
        // TODO: Add a log entry here for the value
        return value;

    }
}
