package com.ha.mail.domain;

import javax.persistence.*;

/**
 * 个性化年度账单记录
 * User: shuiqing
 * DateTime: 17/4/24 下午2:37
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Entity
@Table(name="t_YearBillMailRecord")
public class YearBillMailRecord extends EmailRecord {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    //年度
    private String year;
    //账单类型
    private BillType billType;
    //账单日
    private String billDate;

    public YearBillMailRecord(String reciver, String subject, String content, ContentType contentType) {
        super(reciver, subject, content, contentType);
    }

    public YearBillMailRecord(String reciver, String subject, String content,
                              ContentType contentType, String year, BillType billType, String billDtae) {
        super(reciver, subject, content, contentType);
        this.year = year;
        this.billType = billType;
        this.billDate = billDtae;
    }

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

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }
}
