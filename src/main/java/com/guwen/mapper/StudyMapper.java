package com.guwen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guwen.model.*;
import com.guwen.model.Dto.DetailDto;
import com.guwen.model.Dto.StudyDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudyMapper extends BaseMapper<Word> {

    String startRecordPage(StudyDto study);

    void insertRecord(StudyDto study);

    List<Word> SelectRequestion(int bookId, int startPage, int selectNum);

    List<Explain> selectByWordId(Integer wordId);

    List<Explain> selectOtherExplain();

    List<DetailDto> everyStudyRecord(DetailDto detailDto);

    List<Content> selectAllWord();

}
