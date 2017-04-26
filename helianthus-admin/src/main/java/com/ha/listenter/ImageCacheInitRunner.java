package com.ha.listenter;

import com.ha.base.AdminConstants;
import com.ha.cache.ImageCacheHelper;
import com.ha.mail.base.EmailHelper;
import com.ha.mail.domain.ContentType;
import com.ha.mail.domain.YearBillMailRecord;
import com.ha.mail.repository.YearBillMailRecordRepository;
import com.ha.mail.service.IEmailSenderService;
import com.ha.system.domain.FileStore;
import com.ha.system.service.IFileStoreService;
import com.ha.util.FileUtil;
import org.simplejavamail.email.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import java.io.File;
import java.util.List;

/**
 * User: shuiqing
 * DateTime: 17/4/26 下午3:39
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Component
@Order(value = AdminConstants.STARTUP_RUNNER_IMAGECACHE)
public class ImageCacheInitRunner implements CommandLineRunner {

    private static Logger LOG = LoggerFactory.getLogger(ImageCacheInitRunner.class);

    @Autowired
    private IFileStoreService iFileStoreService;

    @Autowired
    private IEmailSenderService iEmailSenderService;

    @Autowired
    private YearBillMailRecordRepository yearBillMailRecordRepository;

    @Autowired
    private EmailHelper emailHelper;

    @Override
    public void run(String... strings) throws Exception {
        LOG.info("ImageCacheInitRunner 初始化标签图片缓存 start ....... ");
        List<FileStore> fileStoreList = (List<FileStore>) this.iFileStoreService.findAll();
        for(FileStore fs:fileStoreList){
            ImageCacheHelper.setImageCache(fs.getCid(),fs.getContent());
            LOG.info("cid->>" + fs.getCid() + "; content->>" + fs.getContent().toString());
        }
        LOG.info("ImageCacheInitRunner 初始化标签图片缓存 end ....... ");

        /*Email emailNormal = new Email();
        emailNormal.setFromAddress(emailHelper.getSendName(), emailHelper.getSmtpUsername());
        emailNormal.addRecipient("helianthus301", "helianthus301@163.com", Message.RecipientType.TO);
        emailNormal.setTextHTML("<b>We should meet up!</b><img src='cid:thumbsup'>");
        emailNormal.setSubject("Test!");

        byte[] buffer = ImageCacheHelper.getImageCache("thumbsup");

        emailNormal.addEmbeddedImage("thumbsup",buffer,"image/png");
        iEmailSenderService.sendMail(emailNormal);*/

        /*YearBillMailRecord yearBillMailRecord = new YearBillMailRecord(
                "1194208327@qq.com","FFFF",
                "<b>We sfsfsfsfsfs meet up!</b><img src='cid:thumbsup'>",
                "thumbsup",
                ContentType.HTML);
        yearBillMailRecordRepository.save(yearBillMailRecord);*/

        List<YearBillMailRecord> yearBillMailRecordList = (List<YearBillMailRecord>) yearBillMailRecordRepository.findAll();
        for(YearBillMailRecord yearBillMailRecord:yearBillMailRecordList){
            iEmailSenderService.sendYearBillMail(yearBillMailRecord);
        }
    }
}
