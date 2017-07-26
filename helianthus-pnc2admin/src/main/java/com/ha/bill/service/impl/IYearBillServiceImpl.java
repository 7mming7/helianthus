package com.ha.bill.service.impl;

import com.ha.bill.service.IYearBillService;
import com.ha.dimport.domain.YearBillWechatRecord;
import com.ha.dimport.repository.YearBillWechatRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * User: shuiqing
 * DateTime: 17/5/8 下午2:06
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Service
@Transactional
public class IYearBillServiceImpl implements IYearBillService {

    @Autowired
    private YearBillWechatRecordRepository yearBillWechatRecordRepository;

    public YearBillWechatRecordRepository getYearBillWechatRecordRepository() {
        return yearBillWechatRecordRepository;
    }

    public void setYearBillWechatRecordRepository(YearBillWechatRecordRepository yearBillWechatRecordRepository) {
        this.yearBillWechatRecordRepository = yearBillWechatRecordRepository;
    }

    @Override
    public YearBillWechatRecord findByIDNo(String id_no) {
        return yearBillWechatRecordRepository.fetchBillByIdNo(id_no);
    }
}
