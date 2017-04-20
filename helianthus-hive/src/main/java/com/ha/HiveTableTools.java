package com.ha;

import com.ha.base.AbstractTableCreate;
import com.ha.base.HiveTableCreate;
import com.ha.base.TableInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;

/**
 * hive 表工具
 * User: shuiqing
 * DateTime: 17/4/10 上午11:24
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class HiveTableTools {

    private static final void printUsage() {
        String usage = "Usage : Generate hive create script createAll <outfile>|create <tableName> <outfile>|recreateAll <outfile>|recreate <tableName> <outfile>|deleteAll <outfile>|delete <tableName> <outfile>";
        System.out.println(usage);
        System.exit(-1);
    }

    /**
     * @param args
     * @throws IOException
     * @throws URISyntaxException
     */
    public static void main(String[] args) throws IOException, URISyntaxException {
        if (args.length < 1) {
            printUsage();
        }
        String command = args[0];
        TableInfo tableInfo = null;
        AbstractTableCreate c = new HiveTableCreate();
        OutputStream out = null;
        if (command.equalsIgnoreCase("createAll")) {
            out = create(args[1]);
            try {
                tableInfo = c.createAll(out);
            } finally {
                out.close();
            }
        } else if (command.equalsIgnoreCase("create")) {
            String tableName = args[1];
            out = create(args[2]);
            try {
                tableInfo = c.create(out, tableName);
            } finally {
                out.close();
            }
        } else if (command.equalsIgnoreCase("recreateAll")) {
            out = create(args[1]);
            try {
                tableInfo = c.reCreateAll(out);
            } finally {
                out.close();
            }
        } else if (command.equalsIgnoreCase("recreate")) {
            String tableName = args[1];
            out = create(args[2]);
            try {
                tableInfo = c.recreate(out, tableName);
            } finally {
                out.close();
            }
        }else if (command.equalsIgnoreCase("deleteAll")) {
            out = create(args[1]);
            try {
                tableInfo = c.deleteAll(out);
            } finally {
                out.close();
            }
        }else if (command.equalsIgnoreCase("delete")) {
            String tableName = args[1];
            out = create(args[2]);
            try {
                tableInfo = c.delete(out, tableName);
            } finally {
                out.close();
            }
        }else{
            throw new UnsupportedOperationException(command);
        }
        System.out.println(command + " end :"+ tableInfo);
    }

    private static final OutputStream create(String arg) throws IOException {
        File outFile = new File(arg);
        if (!outFile.getParentFile().exists()) {
            outFile.getParentFile().mkdirs();
        }
        return new FileOutputStream(outFile);
    }
}
