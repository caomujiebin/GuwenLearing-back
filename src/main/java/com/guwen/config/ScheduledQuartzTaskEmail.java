package com.guwen.config;

import com.guwen.mapper.ReviewMapper;
import com.guwen.model.Dto.ReviewDto;
import com.guwen.util.MailUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

public class ScheduledQuartzTaskEmail extends QuartzJobBean {
    @Autowired
    private ReviewMapper reviewMapper;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //每天8点给未打卡学习的用户发邮箱题型
        //reviewMapper.updateBetweenDay();
       List<ReviewDto> emailList =  reviewMapper.selectNotReadEmail();
        try {
            for(ReviewDto reviewDto:emailList){
                String content = "亲爱的"+reviewDto.getUserName()+",今天有"+reviewDto.getNewWordsNum()+"个新单词和"+reviewDto.getReviewWordsNum()+"个旧单词待你来学习哦";
                System.out.println(content);
                MailUtil.sendMsg(reviewDto.getEmail(),"今天的古文学习还没打卡呢,快来打卡吧！",content);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
