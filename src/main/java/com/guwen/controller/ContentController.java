package com.guwen.controller;

import com.guwen.model.Book;
import com.guwen.model.Explain;
import com.guwen.model.Word;
import com.guwen.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class ContentController {

    @Autowired
    private ContentService contentService;

    @ResponseBody
    @GetMapping("/parse")
    public Boolean parse() throws IOException {
        return contentService.parseContent();
    }


    //@ResponseBody
/*    @PostMapping({"/study/h_search"})
    public List<Map<String, Object>> highlightParse(@RequestBody Explain explain) throws IOException {
        return contentService.highlightSearch(explain.getWordSentence(),0,10);
    }*/
}