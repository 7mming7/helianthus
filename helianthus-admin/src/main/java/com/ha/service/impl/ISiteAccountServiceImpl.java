package com.ha.service.impl;

import com.ha.domain.SiteAccount;
import com.ha.repository.SiteAccountRepository;
import com.ha.service.ISiteAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 账号service实现
 * User: shuiqing
 * DateTime: 16/8/24 上午10:11
 * Email: helianthus301@163.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Service
@Transactional
public class ISiteAccountServiceImpl implements ISiteAccountService{

    @Autowired
    private SiteAccountRepository siteAccountRepository;

    @Override
    public Page<SiteAccount> querySiteAccounts(int pageIndex, int pageSize) {
        return siteAccountRepository.findAll(new PageRequest(pageIndex,pageSize));
    }

    @Override
    public boolean addOrUpdateAccount(SiteAccount siteAccount) {
        return siteAccountRepository.save(siteAccount) != null;
    }

    @Override
    public boolean deleteAccountByIds(List<String> deleteIds) {
        for (String deleteId : deleteIds) {
            siteAccountRepository.delete(deleteId);
        }
        return true;
    }

    @Override
    public SiteAccount findById(String id) {
        return siteAccountRepository.findOne(id);
    }
}
