package com.ha.dimport.domain;

import com.ha.mail.domain.BillType;

import javax.persistence.*;

/**
 * 年账单数据导入基础表
 * User: shuiqing
 * DateTime: 17/4/27 下午1:38
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Entity
@Table(name = "t_ImportTableYear")
public class ImportTableYear extends ImportTableEvent{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private int year;

    private BillType billType = BillType.YEAR;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public BillType getBillType() {
        return billType;
    }

    public void setBillType(BillType billType) {
        this.billType = billType;
    }
}