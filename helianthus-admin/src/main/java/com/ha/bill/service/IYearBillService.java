package com.ha.bill.service;

import com.ha.dimport.domain.YearBillWechatRecord;
import org.springframework.stereotype.Service;

/**
 * User: shuiqing
 * DateTime: 17/5/8 下午1:48
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface IYearBillService {

    YearBillWechatRecord findByIDNo(String id_no);
}
