package com.guwen.controller;

import com.guwen.model.Book;
import com.guwen.model.Dto.StudyDto;
import com.guwen.model.Explain;
import com.guwen.model.User;
import com.guwen.service.StudyService;
import com.guwen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
public class StudyController {
    @Autowired
    private StudyService studyService;

    //初始化学习界面
    @PostMapping({"/study/InitialStudy"})
    public HashMap<String,Object> login(@RequestBody StudyDto study) {
        HashMap<String,Object> res = studyService.InitialStudy(study);
        return res;
    }

    //每日打卡记录
    @PostMapping({"/study/everyStudyRecord"})
    public List<Object> everyStudyRecord(@RequestBody User user) {
        List<Object> res = studyService.everyStudyRecord(user);
        return res;
    }

    @PostMapping({"/study/h_search"})
    public Map<String, Object> highlightParse(@RequestBody Explain explain) throws IOException {
        HashMap<String,Object> res = new HashMap<>();
        List<Map<String, Object>> wordSentence =  studyService.SearchByWord(explain.getWordSentence(),"wordSentence",0,100);
        List<Map<String, Object>> wordName =  studyService.SearchByWord(explain.getWordSentence(),"wordName",0,100);
        res.put("wordName",wordName);
        res.put("wordSentence",wordSentence);
        return res;
    }
}
