package com.guwen.service.Impl;

import com.guwen.mapper.ForgetMapper;
import com.guwen.mapper.ReviewMapper;
import com.guwen.model.Details;
import com.guwen.model.Dto.StudyDto;
import com.guwen.model.Forget;
import com.guwen.model.Record;
import com.guwen.service.ForgetService;
import com.guwen.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewMapper reviewMapper;


}
