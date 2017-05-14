package com.ha.dimport.repository;

import com.ha.dimport.domain.YearBillWechatRecord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * User: shuiqing
 * DateTime: 17/5/3 下午4:36
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface YearBillWechatRecordRepository extends CrudRepository<YearBillWechatRecord, String> {

    /** 根据编码查询测点 */
    @Query("select m from YearBillWechatRecord m where id_no = ?1")
    YearBillWechatRecord fetchBillByIdNo(String id_no);

}
