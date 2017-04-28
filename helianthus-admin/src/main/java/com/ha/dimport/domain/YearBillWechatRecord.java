package com.ha.dimport.domain;

import com.ha.mail.domain.BillType;

import javax.persistence.*;

/**
 * 微信年度账单记录
 * User: shuiqing
 * DateTime: 17/4/28 上午10:21
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Entity
@Table(name = "t_YearBillWechatRecord")
public class YearBillWechatRecord extends BillWechatRecordBase {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String year;

    private BillType billType = BillType.YEAR;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public BillType getBillType() {
        return billType;
    }

    public void setBillType(BillType billType) {
        this.billType = billType;
    }
}
