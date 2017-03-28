package com.ha.listenter;

import com.ha.base.AdminConstants;
import com.ha.config.HelianthusConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * application初始化执行器
 * User: shuiqing
 * DateTime: 17/3/27 下午3:10
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Component
@Order(value = AdminConstants.STARTUP_RUNNER_INIT)
public class HelianthusInitRunner implements CommandLineRunner {

    private static Logger LOG = LoggerFactory.getLogger(HelianthusInitRunner.class);

    @Override
    public void run(String... args) throws Exception {
        LOG.info(">>>>>>>>>>>>>>>Init ->> 服务启动执行，执行初始化配置等操作 <<<<<<<<<<<<<");

        LOG.info(">>>>>>>>>>>>>>>Init HelianthusConifg start<<<<<<<<<<<<<<<<<<<<<<<<");
        HelianthusConfig.loadConfig();
        LOG.info("HdfsAddr" + HelianthusConfig.getHdfsAddr());
        LOG.info(">>>>>>>>>>>>>>>Init HelianthusConifg end<<<<<<<<<<<<<<<<<<<<<<<<<<");

    }
}
