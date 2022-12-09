package com.guwen.controller;

import com.guwen.model.Book;
import com.guwen.service.BookService;
import com.guwen.service.StudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class BookController {
    @Autowired
    private BookService bookService;

    //根据书本id找到书本图片
    @PostMapping({"/book/selectBookImgById"})
    public String selectBookImgById(@RequestBody Book book){
        String bookImg = bookService.selectBookImgById(book);
        return  bookImg;
    }
}
