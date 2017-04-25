package com.ha.mail.repository;

import com.ha.mail.domain.YearBillMailRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * User: shuiqing
 * DateTime: 17/4/24 下午2:50
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Repository
public interface YearBillMailRecordRepository extends CrudRepository<YearBillMailRecord, String> {
}
