package com.guwen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guwen.model.Details;
import com.guwen.model.Forget;
import com.guwen.model.Record;
import com.guwen.model.Word;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ForgetMapper extends BaseMapper<Forget> {

    void updateStudyRecord(Record record);

    void saveForgetRecord(Forget forget);

    void updateForgetRecord(Forget forget);

    String selectForgetRecord(Forget forget);

    void updateStudyDetails(Details details);

    String selectStudyRecord(Record record);

    String selectStudyDetails(Details details);

    void saveStudyRecord(Record record);

    void saveStudyDetails(Details details);

    List<Word> SelectRequestionFromForget(Integer userId, Integer startPage,Integer selectNum);
}
