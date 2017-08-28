package com.ha.hadoop;

import com.ha.util.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.io.Writable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * User: shuiqing
 * DateTime: 17/8/23 下午4:52
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class HadoopUtil {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(HadoopUtil.class);
    private static final transient ThreadLocal<Configuration> hadoopConfig = new ThreadLocal<Configuration>();

    public static void setCurrentConfiguration(Configuration conf) {
        hadoopConfig.set(conf);
    }

    public static Configuration getCurrentConfiguration() {
        if (hadoopConfig.get() == null) {
            Configuration conf = healSickConfig(new Configuration());
            // do not cache this conf, or will affect following mr jobs
            return conf;
        }
        Configuration conf = hadoopConfig.get();
        return conf;
    }

    private static Configuration healSickConfig(Configuration conf) {
        if (StringUtils.isBlank(conf.get("hadoop.tmp.dir"))) {
            conf.set("hadoop.tmp.dir", "/tmp");
        }
        if (StringUtils.isBlank(conf.get("hbase.fs.tmp.dir"))) {
            conf.set("hbase.fs.tmp.dir", "/tmp");
        }
        return conf;
    }

    public static FileSystem getFileSystem(String path) throws IOException {
        return getFileSystem(new Path(makeURI(path)));
    }

    public static FileSystem getFileSystem(Path path) throws IOException {
        Configuration conf = getCurrentConfiguration();
        return getFileSystem(path, conf);
    }

    public static FileSystem getFileSystem(Path path, Configuration conf) {
        try {
            return path.getFileSystem(conf);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static URI makeURI(String filePath) {
        try {
            return new URI(fixWindowsPath(filePath));
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Cannot create FileSystem from URI: " + filePath, e);
        }
    }

    public static String fixWindowsPath(String path) {
        // fix windows path
        if (path.startsWith("file://") && !path.startsWith("file:///") && path.contains(":\\")) {
            path = path.replace("file://", "file:///");
        }
        if (path.startsWith("file:///")) {
            path = path.replace('\\', '/');
        }
        return path;
    }

    /**
     * @param table the identifier of hive table, in format <db_name>.<table_name>
     * @return a string array with 2 elements: {"db_name", "table_name"}
     */
    public static String[] parseHiveTableName(String table) {
        int cut = table.indexOf('.');
        String database = cut >= 0 ? table.substring(0, cut).trim() : "DEFAULT";
        String tableName = cut >= 0 ? table.substring(cut + 1).trim() : table.trim();

        return new String[] { database, tableName };
    }

    public static void deletePath(Configuration conf, Path path) throws IOException {
        FileSystem fs = FileSystem.get(path.toUri(), conf);
        if (fs.exists(path)) {
            fs.delete(path, true);
        }
    }

    public static byte[] toBytes(Writable writable) {
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(bout);
            writable.write(out);
            out.close();
            bout.close();
            return bout.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Path getFilterOnlyPath(FileSystem fs, Path baseDir, final String filter) throws IOException {
        if (fs.exists(baseDir) == false) {
            return null;
        }

        FileStatus[] fileStatus = fs.listStatus(baseDir, new PathFilter() {
            @Override
            public boolean accept(Path path) {
                return path.getName().startsWith(filter);
            }
        });

        if (fileStatus.length == 1) {
            return fileStatus[0].getPath();
        } else {
            return null;
        }
    }
}
