package com.ha.hjob.hadoop;

import com.ha.base.CommonJobProperties;
import com.ha.hjob.JavaProcessJob;
import com.ha.util.StringUtils;
import com.ha.utils.Props;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * User: shuiqing
 * DateTime: 17/8/15 下午1:36
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class HadoopHiveJob extends JavaProcessJob {

    public static final String HIVE_SCRIPT = "hive.script";
    private static final String HIVECONF_PARAM_PREFIX = "hiveconf.";
    private static final String HIVEVAR_PARAM_PREFIX = "hivevar.";

    private boolean obtainTokens = false;
    private File tokenFile = null;

    private boolean debug = false;

    public HadoopHiveJob(String jobid, Props jobProps)
            throws IOException {
        super(jobid, jobProps);

        getJobProps().put(CommonJobProperties.JOB_ID, jobid);

        debug = getJobProps().getBoolean("debug", false);
    }

    @Override
    public void run() throws Exception {

    }

    @Override
    protected String getJavaClass() {
        return null;
    }

    @Override
    protected String getJVMArguments() {
        String args = super.getJVMArguments();

        String typeUserGlobalJVMArgs =
                getJobProps().getString("jobtype.global.jvm.args", null);
        if (typeUserGlobalJVMArgs != null) {
            args += " " + typeUserGlobalJVMArgs;
        }
        String typeUserJVMArgs = getJobProps().getString("jobtype.jvm.args", null);
        if (typeUserJVMArgs != null) {
            args += " " + typeUserJVMArgs;
        }

        return args;
    }

    @Override
    protected String getMainArguments() {
        ArrayList<String> list = new ArrayList<String>();

        // for hiveconf
        Map<String, String> map = getHiveConf();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                list.add("-hiveconf");
                list.add(StringUtils.shellQuote(
                        entry.getKey() + "=" + entry.getValue(), StringUtils.SINGLE_QUOTE));
            }
        }

        if (debug) {
            list.add("-hiveconf");
            list.add("hive.root.logger=INFO,console");
        }

        // for hivevar
        Map<String, String> hiveVarMap = getHiveVar();
        if (hiveVarMap != null) {
            for (Map.Entry<String, String> entry : hiveVarMap.entrySet()) {
                list.add("-hivevar");
                list.add(StringUtils.shellQuote(
                        entry.getKey() + "=" + entry.getValue(), StringUtils.SINGLE_QUOTE));
            }
        }

        list.add("-f");
        list.add(getScript());

        return StringUtils.join((Collection<String>) list, " ");
    }

    @Override
    protected List<String> getClassPaths() {

        List<String> classPath = super.getClassPaths();

        classPath.add(getSourcePathFromClass(Props.class));

        return classPath;
    }

    protected String getScript() {
        return getJobProps().getString(HIVE_SCRIPT);
    }

    protected Map<String, String> getHiveConf() {
        return getJobProps().getMapByPrefix(HIVECONF_PARAM_PREFIX);
    }

    protected Map<String, String> getHiveVar() {
        return getJobProps().getMapByPrefix(HIVEVAR_PARAM_PREFIX);
    }

    private static String getSourcePathFromClass(Class<?> containedClass) {
        File file =
                new File(containedClass.getProtectionDomain().getCodeSource()
                        .getLocation().getPath());

        if (!file.isDirectory() && file.getName().endsWith(".class")) {
            String name = containedClass.getName();
            StringTokenizer tokenizer = new StringTokenizer(name, ".");
            while (tokenizer.hasMoreTokens()) {
                tokenizer.nextElement();
                file = file.getParentFile();
            }

            return file.getPath();
        } else {
            return containedClass.getProtectionDomain().getCodeSource().getLocation()
                    .getPath();
        }
    }

    /**
     * This cancel method, in addition to the default canceling behavior, also kills the MR jobs launched by Hive
     * on Hadoop
     */
    @Override
    public void cancel() throws InterruptedException {
        super.cancel();

        info("Cancel called.  Killing the Hive launched MR jobs on the cluster");
    }
}
