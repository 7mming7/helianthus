package com.ha.base;

import com.ha.entity.AbstractEntity;
import com.ha.entity.search.Searchable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.List;

/**
 * User: shuiqing
 * DateTime: 17/8/10 下午2:45
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface IBaseService<M extends AbstractEntity, ID extends Serializable> {

    M save(M m);

    M saveAndFlush(M m);

    M update(M m);

    void delete(ID id);

    void delete(M m);

    M findOne(ID id);

    boolean exists(ID id);

    long count();

    List<M> findAll();

    List<M> findAll(Sort sort);

    Page<M> findAll(Pageable pageable);

    Page<M> findAll(Searchable searchable);

    List<M> findAllWithNoPageNoSort(Searchable searchable);

    List<M> findAllWithSort(Searchable searchable);

    Long count(Searchable searchable);

    void deleteByIds(List<ID> ids);
}
