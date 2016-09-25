package com.ha.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.ha.quartz.model.ScheduleJobInfo;
import com.ha.quartz.service.QuartzScheduleService;

/**
 * 调度任务控制器
 * User: shuiqing
 * DateTime: 16/8/23 下午4:54
 * Email: helianthus301@163.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Controller
public class JobController {

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

    @RequestMapping("/addScheduleJob")
    public ModelAndView addScheduleJob() {
        return new ModelAndView("add_schedule_job");
    }

    @RequestMapping(value = "/saveScheduleJob", method = RequestMethod.POST)
    @ResponseBody
    public Object saveScheduleJob(@RequestBody ScheduleJobInfo jobInfo) {
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
