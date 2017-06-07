package com.ha.job;

import com.ha.base.BaseService;
import com.ha.inject.annotation.BaseComponent;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: shuiqing
 * DateTime: 17/6/7 下午2:27
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Service
@Getter
@Setter
public class JobService extends BaseService<Job, Long> {

    @BaseComponent
    @Autowired
    private JobRepository jobRepository;

}
