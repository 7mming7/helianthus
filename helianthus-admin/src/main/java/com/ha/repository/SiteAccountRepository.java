package com.ha.repository;

import com.ha.domain.SiteAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Account repository
 * User: shuiqing
 * DateTime: 16/8/24 上午10:13
 * Email: helianthus301@163.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Repository
public interface SiteAccountRepository extends CrudRepository<SiteAccount, String> {

    Page<SiteAccount> findAll(Pageable pageable);
}
