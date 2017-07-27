package com.ha.hjob;

import com.ha.base.CommonJobProperties;
import com.ha.hjob.process.ImportProcess;
import com.ha.hjob.process.ImportProcessBuilder;
import com.ha.utils.Props;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * User: shuiqing
 * DateTime: 17/7/3 下午4:44
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class ProcessJob extends AbstractJob{

    private static Logger _log = LoggerFactory.getLogger(ProcessJob.class);

    private volatile ImportProcess process;

    public static final String COMMAND = "command";

    private static final long KILL_TIME_MS = 5000;

    public static final String ENV_PREFIX = "env.";
    public static final String ENV_PREFIX_UCASE = "ENV.";

    public static final String WORKING_DIR = "working.dir";
    public static final String JOB_PROP_ENV = "JOB_PROP_FILE";
    public static final String JOB_NAME_ENV = "JOB_NAME";
    public static final String JOB_OUTPUT_PROP_FILE = "JOB_OUTPUT_PROP_FILE";

    private static final String JOB_DUMP_PROPERTIES_IN_LOG = "job.dump.properties";
    private static final String SENSITIVE_JOB_PROP_NAME_SUFFIX = "_X";
    private static final String SENSITIVE_JOB_PROP_VALUE_PLACEHOLDER = "[MASKED]";

    protected String _cwd;

    protected volatile Props jobProps;

    @Override
    public String getCommand() {
        return COMMAND;
    }

    public ProcessJob(final String jobId, final Props jobProps) {
        super(jobId);

        this.jobProps = jobProps;

        jobProps.put(CommonJobProperties.JOB_ID, jobId);
    }

    @Override
    public void run() throws Exception {

        List<String> commands = null;
        try {
            commands = getCommandList();
        } catch (Exception e) {
            handleError("Job set up failed " + e.getCause(), e);
        }

        long startMs = System.currentTimeMillis();

        if (commands == null) {
            handleError("There are no commands to execute", null);
        }

        info(commands.size() + " commands to execute.");

        //环境变量
        Map<String, String> envVars = getEnvironmentVariables();

        for (String command : commands) {
            ImportProcessBuilder builder = null;
            command = String.format("%s",command);
            info("Command: " + command);
            builder =
                    new ImportProcessBuilder(partitionCommandLine(command))
                            .setEnv(envVars).setWorkingDir(getCwd()).setLogger(getLog());


            if (builder.getEnv().size() > 0) {
                info("Environment variables: " + builder.getEnv());
            }
            info("Working directory: " + builder.getWorkingDir());

            // print out the Job properties to the job log.
            this.logJobProperties();

            boolean success = false;
            this.process = builder.build();

            try {
                this.process.run();
                success = true;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            } finally {
                this.process = null;
                info("Process completed "
                        + (success ? "successfully" : "unsuccessfully") + " in "
                        + ((System.currentTimeMillis() - startMs) / 1000) + " seconds.");
            }
        }
    }

    /**
     * prints the current Job props to the Job log.
     */
    protected void logJobProperties() {
        if (this.jobProps != null &&
                this.jobProps.getBoolean(JOB_DUMP_PROPERTIES_IN_LOG, false)){
            try {
                Map<String,String> flattenedProps = this.jobProps.getFlattened();
                this.info("******   Job properties   ******");
                this.info(String.format("- Note : value is masked if property name ends with '%s'.",
                        SENSITIVE_JOB_PROP_NAME_SUFFIX ));
                for(Map.Entry<String, String> entry : flattenedProps.entrySet()){
                    String key = entry.getKey();
                    String value = key.endsWith(SENSITIVE_JOB_PROP_NAME_SUFFIX)?
                            SENSITIVE_JOB_PROP_VALUE_PLACEHOLDER :
                            entry.getValue();
                    this.info(String.format("%s=%s",key,value));
                }
                this.info("****** End Job properties  ******");
            } catch (Exception ex){
                _log.error("failed to log job properties ", ex);
            }
        }
    }

    protected List<String> getCommandList() {
        List<String> commands = new ArrayList<String>();
        commands.add(jobProps.getString(COMMAND));
        for (int i = 1; jobProps.containsKey(COMMAND + "." + i); i++) {
            commands.add(jobProps.getString(COMMAND + "." + i));
        }

        return commands;
    }

    @Override
    public void cancel() throws InterruptedException {
        if (process == null)
            throw new IllegalStateException("Not started.");
        boolean killed = process.softKill(KILL_TIME_MS, TimeUnit.MILLISECONDS);
        if (!killed) {
            warn("Kill with signal TERM failed. Killing with KILL signal.");
            process.hardKill();
        }
    }

    protected void handleError(String errorMsg, Exception e) throws Exception {
        error(errorMsg);
        if (e != null) {
            throw new Exception(errorMsg, e);
        } else {
            throw new Exception(errorMsg);
        }
    }

    public Map<String, String> getEnvironmentVariables() {
        Props props = getJobProps();
        Map<String, String> envMap = props.getMapByPrefix(ENV_PREFIX);
        envMap.putAll(props.getMapByPrefix(ENV_PREFIX_UCASE));
        return envMap;
    }

    /**
     * Splits the command into a unix like command line structure. Quotes and
     * single quotes are treated as nested strings.
     *
     * @param command
     * @return
     */
    public static String[] partitionCommandLine(final String command) {
        ArrayList<String> commands = new ArrayList<String>();

        int index = 0;

        StringBuffer buffer = new StringBuffer(command.length());

        boolean isApos = false;
        boolean isQuote = false;
        while (index < command.length()) {
            char c = command.charAt(index);

            switch (c) {
                case ' ':
                    if (!isQuote && !isApos) {
                        String arg = buffer.toString();
                        buffer = new StringBuffer(command.length() - index);
                        if (arg.length() > 0) {
                            commands.add(arg);
                        }
                    } else {
                        buffer.append(c);
                    }
                    break;
                case '\'':
                    if (!isQuote) {
                        isApos = !isApos;
                    } else {
                        buffer.append(c);
                    }
                    break;
                case '"':
                    if (!isApos) {
                        isQuote = !isQuote;
                    } else {
                        buffer.append(c);
                    }
                    break;
                default:
                    buffer.append(c);
            }

            index++;
        }

        if (buffer.length() > 0) {
            String arg = buffer.toString();
            commands.add(arg);
        }

        return commands.toArray(new String[commands.size()]);
    }

    /**
     * initialize temporary and final property file
     *
     * @return {tmpPropFile, outputPropFile}
     */
    public File[] initPropsFiles() {
        // Create properties file with additionally all input generated properties.
        File[] files = new File[2];
        files[0] = createFlattenedPropsFile(_cwd);

        jobProps.put(ENV_PREFIX + JOB_PROP_ENV, files[0].getAbsolutePath());
        jobProps.put(ENV_PREFIX + JOB_NAME_ENV, getId());

        files[1] = createOutputPropsFile(getId(), _cwd);
        jobProps.put(ENV_PREFIX + JOB_OUTPUT_PROP_FILE, files[1].getAbsolutePath());
        return files;
    }

    public File createFlattenedPropsFile(final String workingDir) {
        File directory = new File(workingDir);
        File tempFile = null;
        try {
            // The temp file prefix must be at least 3 characters.
            tempFile = File.createTempFile(getId() + "_props_", "_tmp", directory);
            jobProps.storeFlattened(tempFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temp property file ", e);
        }

        return tempFile;
    }

    public static File createOutputPropsFile(final String id,
                                             final String workingDir) {
        System.err.println("cwd=" + workingDir);

        File directory = new File(workingDir);
        File tempFile = null;
        try {
            tempFile = File.createTempFile(id + "_output_", "_tmp", directory);
        } catch (IOException e) {
            System.err.println("Failed to create temp output property file :\n");
            e.printStackTrace(System.err);
            throw new RuntimeException("Failed to create temp output property file ",
                    e);
        }
        return tempFile;
    }

    public Props getJobProps() {
        return jobProps;
    }

    public String getWorkingDirectory() {
        String workingDir = getJobProps().getString(WORKING_DIR);
        if (workingDir == null) {
            return "";
        }

        return workingDir;
    }

    public String getCwd() {
        return _cwd;
    }
}
