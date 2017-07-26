package com.ha.mail.service;

import com.ha.mail.domain.YearBillMailRecord;
import org.simplejavamail.email.Email;

/**
 * 邮件发送服务
 * User: shuiqing
 * DateTime: 17/4/24 下午3:22
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface IEmailSenderService {

    void sendMail(Email email);

    void sendYearBillMail(YearBillMailRecord yearBillMailRecord);
}