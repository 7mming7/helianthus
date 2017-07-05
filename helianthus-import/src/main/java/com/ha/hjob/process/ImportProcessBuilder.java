package com.ha.hjob.process;

import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: shuiqing
 * DateTime: 17/7/3 下午3:50
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class ImportProcessBuilder {

    private List<String> cmd = new ArrayList<String>();
    private Map<String, String> env = new HashMap<String, String>();
    private String workingDir = System.getProperty("user.dir");
    private Logger logger = LoggerFactory.getLogger(ImportProcess.class);

    private int stdErrSnippetSize = 30;
    private int stdOutSnippetSize = 30;

    public ImportProcessBuilder(String... command) {
        addArg(command);
    }

    public ImportProcessBuilder addArg(String... command) {
        for (String c : command)
            cmd.add(c);
        return this;
    }

    public ImportProcessBuilder setWorkingDir(String dir) {
        this.workingDir = dir;
        return this;
    }

    public ImportProcessBuilder setWorkingDir(File f) {
        return setWorkingDir(f.getAbsolutePath());
    }

    public String getWorkingDir() {
        return this.workingDir;
    }

    public ImportProcessBuilder addEnv(String variable, String value) {
        env.put(variable, value);
        return this;
    }

    public ImportProcessBuilder setEnv(Map<String, String> m) {
        this.env = m;
        return this;
    }

    public Map<String, String> getEnv() {
        return this.env;
    }

    public ImportProcessBuilder setStdErrorSnippetSize(int size) {
        this.stdErrSnippetSize = size;
        return this;
    }

    public ImportProcessBuilder setStdOutSnippetSize(int size) {
        this.stdOutSnippetSize = size;
        return this;
    }

    public int getStdErrorSnippetSize() {
        return this.stdErrSnippetSize;
    }

    public int getStdOutSnippetSize() {
        return this.stdOutSnippetSize;
    }

    public ImportProcessBuilder setLogger(Logger logger) {
        this.logger = logger;
        return this;
    }

    public ImportProcess build() {
        return new ImportProcess(cmd, env, workingDir, logger);
    }

    public List<String> getCommand() {
        return this.cmd;
    }

    public String getCommandString() {
        return Joiner.on(" ").join(getCommand());
    }

    @Override
    public String toString() {
        return "ProcessBuilder(cmd = " + Joiner.on(" ").join(cmd) + ", env = "
                + env + ", cwd = " + workingDir + ")";
    }
}
