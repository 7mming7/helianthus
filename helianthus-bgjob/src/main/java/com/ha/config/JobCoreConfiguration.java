package com.ha.config;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.ha.executor.handler.JobProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 作业核心配置
 * User: shuiqing
 * DateTime: 17/5/10 下午3:20
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class JobCoreConfiguration {

    private final String jobName;

    private final String cron;

    private final String jobParameter;

    private final String description;

    private final JobProperties jobProperties;

    /**
     * 创建简单作业配置构建器.
     *
     * @param jobName 作业名称
     * @param cron 作业启动时间的cron表达式
     * @return 简单作业配置构建器
     */
    public static Builder newBuilder(final String jobName, final String cron) {
        return new Builder(jobName, cron);
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {

        private final String jobName;

        private final String cron;

        private String jobParameter = "";

        private String description = "";

        private final JobProperties jobProperties = new JobProperties();


        /**
         * 设置作业自定义参数.
         *
         * <p>
         * 可以配置多个相同的作业, 但是用不同的参数作为不同的调度实例.
         * </p>
         *
         * @param jobParameter 作业自定义参数
         *
         * @return 作业配置构建器
         */
        public Builder jobParameter(final String jobParameter) {
            if (null != jobParameter) {
                this.jobParameter = jobParameter;
            }
            return this;
        }

        /**
         * 设置作业描述信息.
         *
         * @param description 作业描述信息
         *
         * @return 作业配置构建器
         */
        public Builder description(final String description) {
            if (null != description) {
                this.description = description;
            }
            return this;
        }

        /**
         * 设置作业属性.
         *
         * @param key 属性键
         * @param value 属性值
         *
         * @return 作业配置构建器
         */
        public Builder jobProperties(final String key, final String value) {
            jobProperties.put(key, value);
            return this;
        }

        /**
         * 构建作业配置对象.
         *
         * @return 作业配置对象
         */
        public final JobCoreConfiguration build() {
            Preconditions.checkArgument(!Strings.isNullOrEmpty(jobName), "jobName can not be empty.");
            Preconditions.checkArgument(!Strings.isNullOrEmpty(cron), "cron can not be empty.");
            return new JobCoreConfiguration(jobName, cron, jobParameter, description, jobProperties);
        }
    }

}
