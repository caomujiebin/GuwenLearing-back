package com.guwen.config;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Quartz配置
 *
 * @author zrj
 * @since 2022/4/13
 **/
@Configuration
public class ScheduledQuartzConfig {
    //@Value("${sue.spring.quartz.cron:*/5 * * * * ?}")
    //每天晚上23点59分执行一次
    @Value("${sue.spring.quartz.cron:0 13 14 * * ?}")
    private String reviewCron;

    //每天早上8:00执行一次
    @Value("${sue.spring.quartz.cron:0 13 14 * * ?}")
    private String emailCron;
    /**
     * 创建定时任务
     */
    @Bean
    public JobDetail quartzTestDetail() {
        JobDetail jobDetail = JobBuilder.newJob(ScheduledQuartzReviewTask.class)
                .storeDurably()
                .build();
        return jobDetail;
    }

    @Bean
    public JobDetail quartzEmailDetail() {
        JobDetail jobDetail = JobBuilder.newJob(ScheduledQuartzTaskEmail.class)
                .storeDurably()
                .build();
        return jobDetail;
    }

    /**
     * 创建触发器
     */
    @Bean
    public Trigger quartzTestJobTrigger() {
        //每天23:59分执行一次
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(reviewCron);
        //创建触发器
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(quartzTestDetail())
                .withSchedule(cronScheduleBuilder)
                .build();
        return trigger;
    }

    @Bean
    public Trigger quartzEmailJobTrigger() {
        //每天8:00执行一次
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(emailCron);
        //创建触发器
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(quartzEmailDetail())
                .withSchedule(cronScheduleBuilder)
                .build();
        return trigger;
    }
}

