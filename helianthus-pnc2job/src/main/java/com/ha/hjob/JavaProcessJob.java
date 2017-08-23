package com.ha.hjob;

import com.ha.utils.Props;

import java.util.ArrayList;
import java.util.List;

/**
 * User: shuiqing
 * DateTime: 17/7/4 下午3:59
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class JavaProcessJob extends ProcessJob{

    public static final String CLASSPATH = "classpath";
    public static final String GLOBAL_CLASSPATH = "global.classpaths";
    public static final String JAVA_CLASS = "java.class";
    public static final String MAIN_ARGS = "main.args";

    public static final String INITIAL_MEMORY_SIZE = "Xms";
    public static final String MAX_MEMORY_SIZE = "Xmx";
    public static final String JVM_PARAMS = "jvm.args";
    public static final String GLOBAL_JVM_PARAMS = "global.jvm.args";

    public static final String DEFAULT_INITIAL_MEMORY_SIZE = "64M";
    public static final String DEFAULT_MAX_MEMORY_SIZE = "256M";

    public static String COMMAND = "java";

    @Override
    public String getCommand() {
        return COMMAND;
    }

    public JavaProcessJob(String jobid, Props sysProps, Props jobProps) {
        super(jobid, sysProps, jobProps);
    }

    @Override
    protected List<String> getCommandList() {
        ArrayList<String> list = new ArrayList<String>();
        list.add(createCommandLine());
        return list;
    }

    protected String createCommandLine() {
        String command = COMMAND + " ";
        command += getJVMArguments() + " ";
        command += "-Xms" + getInitialMemorySize() + " ";
        command += "-Xmx" + getMaxMemorySize() + " ";
        command += "-cp " + createArguments(getClassPaths(), ":") + " ";
        command += getJavaClass() + " ";
        command += getMainArguments();

        return command;
    }

    protected String getInitialMemorySize() {
        return getJobProps().getString(INITIAL_MEMORY_SIZE,
                DEFAULT_INITIAL_MEMORY_SIZE);
    }

    protected String getMaxMemorySize() {
        return getJobProps().getString(MAX_MEMORY_SIZE, DEFAULT_MAX_MEMORY_SIZE);
    }

    protected String getJVMArguments() {
        String globalJVMArgs = getJobProps().getString(GLOBAL_JVM_PARAMS, null);

        if (globalJVMArgs == null) {
            return getJobProps().getString(JVM_PARAMS, "");
        }

        return globalJVMArgs + " " + getJobProps().getString(JVM_PARAMS, "");
    }

    protected String getJavaClass() {
        return getJobProps().getString(JAVA_CLASS);
    }

    protected String getClassPathParam() {
        List<String> classPath = getClassPaths();
        if (classPath == null || classPath.size() == 0) {
            return "";
        }

        return "-cp " + createArguments(classPath, ":") + " ";
    }

    protected List<String> getClassPaths() {

        List<String> classPaths = getJobProps().getStringList(CLASSPATH, null, ",");

        ArrayList<String> classpathList = new ArrayList<String>();
        // Adding global properties used system wide.
        if (getJobProps().containsKey(GLOBAL_CLASSPATH)) {
            List<String> globalClasspath =
                    getJobProps().getStringList(GLOBAL_CLASSPATH);
            for (String global : globalClasspath) {
                getLog().info("Adding to global classpath:" + global);
                classpathList.add(global);
            }
        }

        if (classPaths != null) {
            classpathList.addAll(classPaths);
        }

        return classpathList;
    }

    protected String createArguments(List<String> arguments, String separator) {
        if (arguments != null && arguments.size() > 0) {
            String param = "";
            for (String arg : arguments) {
                param += arg + separator;
            }

            return param.substring(0, param.length() - 1);
        }

        return "";
    }

    protected String getMainArguments() {
        return getJobProps().getString(MAIN_ARGS, "");
    }
}
