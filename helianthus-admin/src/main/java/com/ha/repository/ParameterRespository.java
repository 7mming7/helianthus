package com.ha.repository;

import com.ha.domain.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * User: shuiqing
 * DateTime: 17/3/29 下午4:00
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Repository
public interface ParameterRespository extends CrudRepository<Parameter, String> {

    Page<Parameter> findAll(Pageable pageable);
}
