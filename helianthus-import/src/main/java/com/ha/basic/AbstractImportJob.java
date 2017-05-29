package com.ha.basic;

import com.ha.exception.ImportJobException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.Map;

/**
 * Import抽象
 * User: shuiqing父类
 * DateTime: 17/5/27 下午2:40
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Getter
@Setter
@Slf4j
public abstract class AbstractImportJob extends ElasticJob {

    protected static PropertyPlaceholderHelper placeHolderHelper = new PropertyPlaceholderHelper("{", "}");

    @Override
    protected final void executeJob(final ImportExecutionContext importExecutionContext) {
        long start = System.currentTimeMillis();


        long end = System.currentTimeMillis();
    }

    /**
     * 更改作业cron表达式，请确认作业名是正确的。
     * @param jobName 作业名
     * @param cron cron表达式
     * @param customContext 自定义上下文
     * @throws ImportJobException 可能抛的异常有：type为0，表示cron表达式无效；type为1，表示作业名在这个namespace下不存在；type为3，表示customContext内容超出1M；type为4，表示作业名有误，不能为$SaturnExecutors。
     */
    public void updateJobCron(String jobName, String cron, Map<String, String> customContext) throws ImportJobException {
        this.getConfigService().updateJobCron(jobName, cron, customContext);
    }

    /**
     * 实际处理逻辑
     * @param importExecutionContext 上下文
     * @return 每个分片返回一个SaturnJobReturn. 若为null，表示执行失败
     */
    protected abstract Map<Integer, ImportJobReturn> handleJob(ImportExecutionContext importExecutionContext);

    public abstract ImportJobReturn doExecution(String jobName, Integer key, String value,
                                                ImportExecutionContext importExecutionContext) throws Throwable;
}
