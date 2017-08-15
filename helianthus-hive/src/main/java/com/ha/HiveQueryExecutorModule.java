package com.ha;

import org.apache.hadoop.hive.cli.CliSessionState;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.ql.session.SessionState;

import static org.apache.hadoop.hive.conf.HiveConf.ConfVars.HIVEHISTORYFILELOC;
import static org.apache.hadoop.hive.conf.HiveConf.ConfVars.SCRATCHDIR;

/**
 * instance hive connect.
 * User: shuiqing
 * DateTime: 17/8/15 上午11:36
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class HiveQueryExecutorModule {
    private HiveConf hiveConf = null;
    private CliSessionState ss = null;

    HiveConf provideHiveConf() {
        if (this.hiveConf != null) {
            return this.hiveConf;
        } else {
            this.hiveConf = new HiveConf(SessionState.class);
        }

        troublesomeConfig(HIVEHISTORYFILELOC, hiveConf);
        troublesomeConfig(SCRATCHDIR, hiveConf);

        return hiveConf;
    }

    private void troublesomeConfig(HiveConf.ConfVars value, HiveConf hc) {
        System.out.println("Troublesome config " + value + " = "
                + HiveConf.getVar(hc, value));
    }

    CliSessionState provideCliSessionState() {
        if (ss != null) {
            return ss;
        }
        ss = new CliSessionState(provideHiveConf());
        SessionState.start(ss);
        return ss;
    }

    protected void configure() {
        /** Nothing to do **/
    }
}
