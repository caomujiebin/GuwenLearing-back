package com.guwen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guwen.model.Book;
import com.guwen.model.Dto.ReviewDto;
import com.guwen.model.Review;
import com.guwen.model.User;
import com.guwen.model.Word;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewMapper extends BaseMapper<Review> {

    void saveReviewWord(Review review);

    Review selectByUserIdAndWordId(Review review);

    void updateByUserIdAndWordId(Review review);

    void updateBetweenDay();

    List<ReviewDto> selectNotReadEmail();

    List<Word> SelectRequestionFromReview(Integer userId, Integer startPage,Integer selectNum);

    User selectStudyWords(Integer userId);
}
