package com.ha.system.repository;

import com.ha.system.domain.FileStore;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * User: shuiqing
 * DateTime: 17/4/25 下午2:14
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Repository
public interface FileStoreRespository extends CrudRepository<FileStore, String> {
}