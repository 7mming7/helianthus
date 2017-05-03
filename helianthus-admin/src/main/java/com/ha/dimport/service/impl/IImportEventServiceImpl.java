package com.ha.dimport.service.impl;

import com.ha.dimport.repository.ImportTableYearRepository;
import com.ha.dimport.repository.YearBillWechatRecordRepository;
import com.ha.dimport.service.IImportEventService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: shuiqing
 * DateTime: 17/5/3 下午4:32
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class IImportEventServiceImpl implements IImportEventService {

    @Autowired
    private ImportTableYearRepository importTableYearRepository;

    @Autowired
    private YearBillWechatRecordRepository yearBillWechatRecordRepository;
}
