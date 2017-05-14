package com.ha.executor.type;

import com.ha.api.JobContext;
import com.ha.api.simple.SimpleJob;
import com.ha.executor.AbstractElasticJobExecutor;
import com.ha.executor.JobFacade;

/**
 * 简单作业执行器
 * User: shuiqing
 * DateTime: 17/5/11 下午2:55
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public final class SimpleJobExecutor extends AbstractElasticJobExecutor {

    private final SimpleJob simpleJob;

    public SimpleJobExecutor(final SimpleJob simpleJob, final JobFacade jobFacade) {
        super(jobFacade);
        this.simpleJob = simpleJob;
    }

    @Override
    protected void process(final JobContext jobContext) {
        simpleJob.execute(jobContext);
    }
}
