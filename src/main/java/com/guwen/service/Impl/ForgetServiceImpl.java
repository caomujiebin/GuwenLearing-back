package com.guwen.service.Impl;

import com.guwen.mapper.ForgetMapper;
import com.guwen.mapper.ReviewMapper;
import com.guwen.model.Details;
import com.guwen.model.Dto.StudyDto;
import com.guwen.model.Forget;
import com.guwen.model.Record;
import com.guwen.model.Review;
import com.guwen.service.ForgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDate.now;
import static java.time.LocalDate.parse;

@Service
public class ForgetServiceImpl implements ForgetService {
    @Autowired
    private ForgetMapper forgetMapper;

    @Autowired
    private ReviewMapper reviewMapper;

    @Override
    public HashMap<String, Object> saveWordRecord(StudyDto study) throws ParseException {
        HashMap<String, Object> res = new HashMap<>();
        res.put("code",400);
        //插入学习记录
        Record record = new Record(study.getUserId(),study.getBookId(),study.getNewWordsNum());
        String bookPage = forgetMapper.selectStudyRecord(record);
        if(bookPage==null){
            record.setBookPage(study.getNewWordsNum());
            forgetMapper.saveStudyRecord(record);

        }else {
            record.setBookPage(Integer.valueOf(bookPage)+study.getNewWordsNum());
            forgetMapper.updateStudyRecord(record);
        }

        //插入当天学习的单词数
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Details details = new Details(study.getUserId(),study.getNewWordsNum(),date);
        String wordNum = forgetMapper.selectStudyDetails(details);
        if(wordNum==null) {
            details.setWordNum(study.getNewWordsNum());
            forgetMapper.saveStudyDetails(details);
        }
        else {
            details.setWordNum(study.getNewWordsNum()+Integer.valueOf(wordNum));
            forgetMapper.updateStudyDetails(details);
        }

        //插入遗忘的单词
        for(int wordId : study.getForgetList()){
            Forget forget = new Forget();
            forget.setUserId(Integer.valueOf(study.getUserId()));
            forget.setWordId(wordId);
            forget.setForgetStatus(1);
            //如果之前没有这条遗忘记录，执行插入，否则执行更新
            String forgetNum = forgetMapper.selectForgetRecord(forget);
            if(forgetNum==null){
                forget.setForgetNum(1);
                forgetMapper.saveForgetRecord(forget);
            }else{

                forget.setForgetNum(Integer.valueOf(forgetNum)+1);
                forgetMapper.updateForgetRecord(forget);
            }
        }
        //将实词Id集合和遗忘实词集合求差集，找到未遗忘实词Id
        List<Integer> unforgetList = study.getWordIdList().stream().filter(e->{
            return !study.getForgetList().contains(e);
        }).collect(Collectors.toList());
        //删除未遗忘的单词
        for(int wordId : unforgetList){
            Forget forget = new Forget();
            forget.setUserId(Integer.valueOf(study.getUserId()));
            forget.setWordId(wordId);
            String forgetNum = forgetMapper.selectForgetRecord(forget);
            forget.setForgetNum(Integer.valueOf(forgetNum));
            forget.setForgetStatus(0);
            forgetMapper.updateForgetRecord(forget);
        }

        //将单词存放到复习表中
        for(int wordId:study.getWordIdList()){

            Review review = new Review(study.getUserId(),wordId,date,1);
            Review isExist =  reviewMapper.selectByUserIdAndWordId(review);
            if(isExist !=null){
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-mm-dd");
                long gapDay = LocalDate.parse(date).toEpochDay() - LocalDate.parse(isExist.getFirstDate()).toEpochDay();
                review.setGapDay((int)gapDay);
                //review.setBetweenDay(isExist.getBetweenDay()+1);
                reviewMapper.updateByUserIdAndWordId(review);
            }else{
                review.setFirstDate(date);
                review.setGapDay(0);
                review.setBetweenDay(0);
                reviewMapper.insert(review);
            }

            //reviewMapper.saveReviewWord(review);
        }
        res.put("code",200);
        return res;
    }
}
