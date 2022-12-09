package com.guwen.config;

import com.guwen.mapper.ForgetMapper;
import com.guwen.mapper.ReviewMapper;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Quartz执行任务
 *
 * @author zrj
 * @since 2022/4/13
 **/
public class ScheduledQuartzReviewTask extends QuartzJobBean {
    @Autowired
    private ReviewMapper reviewMapper;
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //每隔一天将所有实词复习的间隔天数加一天
        reviewMapper.updateBetweenDay();
    }
}

