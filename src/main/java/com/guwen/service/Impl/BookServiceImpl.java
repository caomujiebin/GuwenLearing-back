package com.guwen.service.Impl;

import com.guwen.mapper.BookMapper;
import com.guwen.mapper.StudyMapper;
import com.guwen.model.Book;
import com.guwen.model.Dto.DetailDto;
import com.guwen.model.Dto.StudyDto;
import com.guwen.model.Dto.WordDto;
import com.guwen.model.Explain;
import com.guwen.model.User;
import com.guwen.model.Word;
import com.guwen.service.BookService;
import com.guwen.service.StudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookMapper bookMapper;


    @Override
    public String selectBookImgById(Book book) {
        return bookMapper.selectById(book.getBookId()).getBookImg();
    }
}
