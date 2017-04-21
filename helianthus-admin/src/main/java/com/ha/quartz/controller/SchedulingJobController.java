package com.ha.quartz.controller;

import com.ha.quartz.domain.AbstractExecutableJob;
import com.ha.quartz.domain.ScheduleJobInfo;
import com.ha.quartz.domain.SimpleExecutableJob;
import com.ha.quartz.service.QuartzScheduleService;
import com.ha.quartz.util.CronExpressionUtil;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * 计划任务controller
 * User: shuiqing
 * DateTime: 17/3/29 下午3:42
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Controller
public class SchedulingJobController {

    private static Logger LOG = LoggerFactory.getLogger(SchedulingJobController.class);

    @Autowired
    private QuartzScheduleService quartzScheduleService;

    @RequestMapping("/viewPlannedJobs")
    public ModelAndView viewScheduledJobs() {
        return new ModelAndView("planned_job_list");
    }

    @RequestMapping("/viewRunningJobs")
    public ModelAndView viewRunningJobs() {
        return new ModelAndView("running_job_list");
    }

    @RequestMapping(value = "/getPlannedJobList", method = RequestMethod.GET)
    @ResponseBody
    public Object getPlannedJobList() {
        return quartzScheduleService.getScheduledJobList();
    }

    @RequestMapping(value = "/getRunningJobList", method = RequestMethod.GET)
    @ResponseBody
    public Object getScheduledJobList() {
        return quartzScheduleService.getRunningJobList();
    }

    @RequestMapping(value = "/saveJob", method = RequestMethod.POST)
    @ResponseBody
    public Object saveJob(ScheduleJobInfo jobInfo) {
        Assert.notNull(jobInfo.getJobName());
        Assert.notNull(jobInfo.getJobGroup());
        Assert.notNull(jobInfo.getJobClass());
        Assert.notNull(jobInfo.getCronExpression());

        String cronExpression = CronExpressionUtil.convertToSpringCron(jobInfo.getCronExpression());
        if (!CronExpressionUtil.isValidExpression(cronExpression))
            throw new RuntimeException("Cron表达式不正确！");

        jobInfo = new ScheduleJobInfo(jobInfo.getJobName(), jobInfo.getJobGroup(), jobInfo.getJobClass(), cronExpression);

        SimpleExecutableJob simpleExecutableJob = new SimpleExecutableJob(jobInfo);
        return quartzScheduleService.addJob(simpleExecutableJob);
    }

    @RequestMapping(value = "/updateJob", method = RequestMethod.POST)
    @ResponseBody
    public Object updateJob(@RequestBody ScheduleJobInfo jobInfo) {
        return quartzScheduleService.updateJob(jobInfo);
    }

    @RequestMapping(value = "/pauseJob", method = RequestMethod.POST)
    @ResponseBody
    public Object pauseJob(@RequestBody ScheduleJobInfo jobInfo) {
        return quartzScheduleService.pauseJob(jobInfo.getJobName(), jobInfo.getJobGroup());
    }
    @RequestMapping(value = "/resumeJob", method = RequestMethod.POST)
    @ResponseBody
    public Object resumeJob(@RequestBody ScheduleJobInfo jobInfo) {
        return quartzScheduleService.resumeJob(jobInfo.getJobName(), jobInfo.getJobGroup());
    }

    @RequestMapping(value = "/deleteJob", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteJob(@RequestBody ScheduleJobInfo jobInfo) {
        return quartzScheduleService.deleteJob(jobInfo.getJobName(), jobInfo.getJobGroup());
    }

    @RequestMapping(value = "/modifyJobCron", method = RequestMethod.POST)
    @ResponseBody
    public Object modifyJobCron(@RequestBody ScheduleJobInfo jobInfo) {
        return null;
    }
}
