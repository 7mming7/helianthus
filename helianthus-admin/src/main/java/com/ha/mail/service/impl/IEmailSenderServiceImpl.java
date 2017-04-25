package com.ha.mail.service.impl;

import com.ha.mail.repository.YearBillMailRecordRepository;
import com.ha.mail.service.IEmailSenderService;
import org.simplejavamail.email.Email;
import org.simplejavamail.mailer.Mailer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * User: shuiqing
 * DateTime: 17/4/24 下午3:24
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Service
@Transactional
public class IEmailSenderServiceImpl implements IEmailSenderService {

    @Autowired
    private Mailer mailer;

    public Mailer getMailer() {
        return mailer;
    }

    public void setMailer(Mailer mailer) {
        this.mailer = mailer;
    }

    @Override
    public void sendMail(Email email) {
        System.out.println("发送邮件");
        mailer.sendMail(email);
    }
}
