package com.ha.quartz.domain;

import com.ha.mail.domain.YearBillMailRecord;
import com.ha.mail.repository.YearBillMailRecordRepository;
import com.ha.mail.service.IEmailSenderService;
import com.ha.util.SpringUtils;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 年度账单邮件发送任务
 * User: shuiqing
 * DateTime: 17/4/26 下午5:06
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class YearBillEmailSendJob extends AbstractExecutableJob {

    private static final long serialVersionUID = 8812093165243944697l;

    public YearBillEmailSendJob(ScheduleJobInfo jobInfo) {
        super(jobInfo);
    }

    @Override
    public boolean execute(JobExecutionContext context) {
        IEmailSenderService iEmailSenderService = SpringUtils.getBean(IEmailSenderService.class);
        YearBillMailRecordRepository yearBillMailRecordRepository = SpringUtils.getBean(YearBillMailRecordRepository.class);
        List<YearBillMailRecord> yearBillMailRecordList = (List<YearBillMailRecord>) yearBillMailRecordRepository.findAll();
        for(YearBillMailRecord yearBillMailRecord:yearBillMailRecordList){
            iEmailSenderService.sendYearBillMail(yearBillMailRecord);
        }

        return true;
    }
}
