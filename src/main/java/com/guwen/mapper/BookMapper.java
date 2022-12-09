package com.guwen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guwen.model.Book;
import com.guwen.model.Dto.DetailDto;
import com.guwen.model.Dto.StudyDto;
import com.guwen.model.Explain;
import com.guwen.model.Word;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookMapper extends BaseMapper<Book> {

}
