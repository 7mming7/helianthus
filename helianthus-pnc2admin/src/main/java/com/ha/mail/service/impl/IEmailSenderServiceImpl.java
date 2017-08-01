package com.ha.mail.service.impl;

import com.ha.mail.base.EmailHelper;
import com.ha.mail.service.IEmailSenderService;
import org.simplejavamail.email.Email;
import org.simplejavamail.mailer.Mailer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * email发送业务类
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

    private static Logger LOG = LoggerFactory.getLogger(IEmailSenderServiceImpl.class);

    @Autowired
    private Mailer mailer;

    @Autowired
    private EmailHelper emailHelper;

    public Mailer getMailer() {
        return mailer;
    }

    public void setMailer(Mailer mailer) {
        this.mailer = mailer;
    }

    @Override
    public void sendMail(Email email) {
        LOG.info("send mail " + email.toString());
        mailer.sendMail(email);
        LOG.info("send mail success.");
    }
}
