package com.ha.hjob.hadoop;

import com.ha.base.CommonJobProperties;
import com.ha.hjob.JavaProcessJob;
import com.ha.utils.Props;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * hadoop mr job.
 * User: shuiqing
 * DateTime: 17/8/23 下午2:32
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class HadoopJavaJob extends JavaProcessJob {

    public static final String RUN_METHOD_PARAM = "method.run";
    public static final String CANCEL_METHOD_PARAM = "method.cancel";
    public static final String PROGRESS_METHOD_PARAM = "method.progress";

    public static final String JOB_CLASS = "job.class";
    public static final String DEFAULT_CANCEL_METHOD = "cancel";
    public static final String DEFAULT_RUN_METHOD = "run";
    public static final String DEFAULT_PROGRESS_METHOD = "getProgress";

    private String _runMethod;
    private String _cancelMethod;
    private String _progressMethod;

    private Object _javaObject = null;

    private boolean obtainTokens = false;
    private File tokenFile = null;

    public HadoopJavaJob(String jobid, Props sysProps, Props jobProps)
            throws RuntimeException {
        super(jobid, sysProps, jobProps);

        getJobProps().put(CommonJobProperties.JOB_ID, jobid);
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
    protected List<String> getClassPaths() {
        List<String> classPath = new ArrayList<String>();


        return classPath;
    }

    private void mergeClassPaths(List<String> classPath,
                                 List<String> typeClassPath) {
        if (typeClassPath != null) {
            // fill in this when load this jobtype
            String pluginDir = getSysProps().get("plugin.dir");
            for (String jar : typeClassPath) {
                File jarFile = new File(jar);
                if (!jarFile.isAbsolute()) {
                    jarFile = new File(pluginDir + File.separatorChar + jar);
                }

                if (!classPath.contains(jarFile.getAbsoluteFile())) {
                    classPath.add(jarFile.getAbsolutePath());
                }
            }
        }
    }
}
