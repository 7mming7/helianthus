package com.ha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Application startup.
 * User: shuiqing
 * DateTime: 16/8/17 下午2:20
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@SpringBootApplication
@RestController
@ImportResource(locations = {"classpath*:spring/*.xml"})
public class Application {

    @RequestMapping("/")
    public ModelAndView greeting() {
        return new ModelAndView("index");
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}