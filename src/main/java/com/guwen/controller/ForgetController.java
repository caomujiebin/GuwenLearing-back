package com.guwen.controller;

import com.guwen.model.Dto.StudyDto;
import com.guwen.model.Forget;
import com.guwen.service.ForgetService;
import com.guwen.service.StudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.HashMap;

@CrossOrigin
@RestController
public class ForgetController {
    @Autowired
    private ForgetService forgetService;
    //保存学习记录
    @PostMapping({"/forget/saveWordRecord"})
    public HashMap<String,Object> saveWordRecord(@RequestBody StudyDto study) throws ParseException {
        HashMap<String,Object> res = forgetService.saveWordRecord(study);
        return res;
    }
}
