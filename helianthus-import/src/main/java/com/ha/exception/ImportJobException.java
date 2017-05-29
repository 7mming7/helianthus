package com.ha.exception;

/**
 * User: shuiqing
 * DateTime: 17/5/27 下午3:07
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class ImportJobException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final int CRON_VALID = 0;

    public static final int JOB_NOT_FOUND = 1;

    public static final int JOBNAME_VALID = 2;

    private int type;

    private String message;

    public ImportJobException(int type, String message) {
        super();
        this.type = type;
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
