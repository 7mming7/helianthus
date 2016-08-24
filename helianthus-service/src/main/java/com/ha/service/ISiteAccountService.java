package com.ha.service;

import com.ha.domain.SiteAccount;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 账号service接口
 * User: shuiqing
 * DateTime: 16/8/24 上午9:53
 * Email: helianthus301@163.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface ISiteAccountService {

    public Page<SiteAccount> querySiteAccounts(int pageIndex, int pageSize);

    public boolean addAccount(SiteAccount siteAccount);

    public boolean deleteAccountByIds(List<String> deleteIds);
}
