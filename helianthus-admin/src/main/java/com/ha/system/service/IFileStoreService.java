package com.ha.system.service;

import com.ha.system.domain.FileStore;

/**
 * User: shuiqing
 * DateTime: 17/4/25 下午2:17
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface IFileStoreService {

    public boolean addOrUpdateFileStore(FileStore fileStore);

    public Iterable<FileStore> findAll();
}