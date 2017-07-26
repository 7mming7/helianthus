package com.ha.system.service.impl;

import com.ha.system.domain.FileStore;
import com.ha.system.repository.FileStoreRespository;
import com.ha.system.service.IFileStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * User: shuiqing
 * DateTime: 17/4/25 下午2:18
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Service
@Transactional
public class IFileStoreServiceImpl implements IFileStoreService {

    @Autowired
    private FileStoreRespository fileStoreRespository;

    public FileStoreRespository getFileStoreRespository() {
        return fileStoreRespository;
    }

    public void setFileStoreRespository(FileStoreRespository fileStoreRespository) {
        this.fileStoreRespository = fileStoreRespository;
    }

    @Override
    public boolean addOrUpdateFileStore(FileStore fileStore) {
        return fileStoreRespository.save(fileStore) != null;
    }

    @Override
    public Iterable<FileStore> findAll(){
        return fileStoreRespository.findAll();
    }
}
