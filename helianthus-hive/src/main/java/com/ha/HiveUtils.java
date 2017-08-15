package com.ha;

import org.apache.hadoop.hive.cli.CliDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * User: shuiqing
 * DateTime: 17/8/15 上午11:33
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class HiveUtils {
    private final static Logger LOG =
            LoggerFactory.getLogger(HiveUtils.class);

    private HiveUtils() {
    }

    public static HiveQueryExecutor getHiveQueryExecutor() {
        HiveQueryExecutorModule hqem = new HiveQueryExecutorModule();
        try {
            return new RealHiveQueryExecutor(hqem.provideHiveConf(),
                    hqem.provideCliSessionState(), new CliDriver());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Normally hive.aux.jars.path is expanded from just being a path to the full
     * list of files in the directory by the hive shell script. Since we normally
     * won't be running from the script, it's up to us to do that work here. We
     * use a heuristic that if there is no occurrence of ".jar" in the original,
     * it needs expansion. Otherwise it's already been done for us.
     *
     * Also, surround the files with uri niceities.
     */
    static String expandHiveAuxJarsPath(String original) throws IOException {
        if (original == null || original.contains(".jar"))
            return original;

        File[] files = new File(original).listFiles();

        if (files == null || files.length == 0) {
            LOG.info("No files in to expand in aux jar path. Returning original parameter");
            return original;
        }

        return filesToURIString(files);

    }

    static String filesToURIString(File[] files) throws IOException {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < files.length; i++) {
            sb.append("file:///").append(files[i].getCanonicalPath());
            if (i != files.length - 1)
                sb.append(",");
        }

        return sb.toString();
    }
}
