package com.ha.config;

import com.ha.basic.AbstractImportService;
import com.ha.basic.JobScheduler;
import com.ha.exception.ImportJobException;
import com.ha.util.JsonUtil;
import org.codehaus.jackson.map.type.MapType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.quartz.CronExpression;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * 分布式作业配置服务
 * User: shuiqing
 * DateTime: 17/5/27 上午9:52
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class ConfigurationService extends AbstractImportService {

    private static final String DOUBLE_QUOTE = "\"";

    //参考http://stackoverflow.com/questions/17963969/java-regex-pattern-split-commna
    private static final String PATTERN = ",(?=(([^\"]*\"){2})*[^\"]*$)";

    private MapType customContextType = TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, String.class);

    public ConfigurationService(JobScheduler jobScheduler) {
        super(jobScheduler);
    }

    /**
     * 获取作业自定义参数.
     *
     * @return 作业自定义参数
     */
    public String getJobParameter() {
        return jobConfiguration.getJobParameter();
    }

    /**
     * 获取作业启动时间的cron表达式.
     *
     * @return 作业启动时间的cron表达式
     */
    public String getCron() {
        return jobConfiguration.getCron();
    }

    /**
     * 将str转为map
     * @param customContextStr str字符串
     * @return 自定义上下文map
     */
    private Map<String, String> toCustomContext(String customContextStr) {
        Map<String, String> customContext = null;
        if(customContextStr != null) {
            customContext = JsonUtil.fromJSON(customContextStr, customContextType);
        }
        if(customContext == null) {
            customContext = new HashMap<>();
        }
        return customContext;
    }

    /**
     * 将map转为str字符串
     * @param customContextMap 自定义上下文map
     * @return 自定义上下文str
     */
    private String toCustomContext(Map<String, String> customContextMap) {
        String result = JsonUtil.toJSON(customContextMap);
        if(result == null) {
            result = "";
        }
        return result.trim();
    }

    /**
     * 更新作业的cron表达式
     * @param jobName 作业名
     * @param cron cron表达式
     * @param customContext 自定义上下文
     * @throws ImportJobException 可能抛的异常有：type为0，表示cron表达式无效；type为1，表示作业名在这个namespace下不存在；type为3，表示customContext内容超出1M。
     */
    public void updateJobCron(String jobName, String cron, Map<String, String> customContext) throws ImportJobException {
        String cron0 = cron;
        if(cron0 != null && !cron0.trim().isEmpty()) {
            try {
                cron0 = cron0.trim();
                CronExpression.validateExpression(cron0);
            } catch (ParseException e) {
                throw new ImportJobException(ImportJobException.CRON_VALID, "The cron expression is valid: " + cron);
            }
        } else {
            cron0 = "";
        }
    }
}
