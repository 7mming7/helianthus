package com.ha.bill.controller;

import com.alibaba.fastjson.JSON;
import com.ha.bill.service.IYearBillService;
import com.ha.dimport.domain.YearBillWechatRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 年度账单controller
 * User: shuiqing
 * DateTime: 17/5/8 下午1:47
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@RestController
@RequestMapping(value = "/httpService")
public class YearBillController {

    @Autowired
    private IYearBillService yearBillService;

    public IYearBillService getYearBillService() {
        return yearBillService;
    }

    public void setYearBillService(IYearBillService yearBillService) {
        this.yearBillService = yearBillService;
    }

    /**
     * 获取用户的年度账单数据
     * @param idNo 证件号
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/yearBill/{id_no}",method = RequestMethod.GET,produces="application/json;charset=UTF-8")
    public String fetchUserYearBill(@PathVariable("id_no") String idNo){
        if(!StringUtils.isBlank(idNo)){
            YearBillWechatRecord yearBillWechatRecord = yearBillService.findByIDNo(idNo);
            String yearBillRecordString = JSON.toJSONString(yearBillWechatRecord);
            return yearBillRecordString;
        }

        return null;
    }
}
