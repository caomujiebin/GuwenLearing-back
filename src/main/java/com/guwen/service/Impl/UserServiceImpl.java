package com.guwen.service.Impl;

import com.guwen.mapper.StudyMapper;
import com.guwen.mapper.UserMapper;
import com.guwen.model.Dto.DetailDto;
import com.guwen.model.Dto.UserDto;
import com.guwen.model.User;
import com.guwen.service.UserService;

import com.guwen.util.DefaultUtil;
import com.guwen.util.Md5SaltUtil;
import com.guwen.util.RegexUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StudyMapper studyMapper;
    @Override
    public HashMap<String, Object> login(User user) {
        String email = user.getEmail();
        HashMap<String, Object> res =new HashMap<>();
        String pas = user.getPassword();
        String password = Md5SaltUtil.shaHex(pas,Md5SaltUtil.generateSalt());
        //如果查找不到，则返回200
        user = userMapper.login(email,password);
        if(user!=null){
            res.put("code", 200);
            user.setPassword(pas);
            res.put("data",user);
            res.put("studyRecord",everyStudyRecord(user.getUserId()));
        }
        else res.put("code", 400);

        return res;
    }


    @Override
    public HashMap<String, Object> signUp(UserDto user) {
        HashMap<String, Object> res = new HashMap<>();
        //判断邮箱地址是否合法
        /*String regex_email="[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+";
        String regex_password = ".*[a-z][1,].*";*/
        res.put("code",400);
        if(!user.getEmail().matches(RegexUtil.regex_email)){
            res.put("error","不是一个合法的邮箱地址");
        }
       else if(!user.getPassword().matches(RegexUtil.regex_password)){
            res.put("error","密码必须包含数字和字母");
        }
        //判断两次密码是否一致
        else if(!user.getPassword().equals(user.getRePassword())){
            res.put("error","两次密码输入不一致");
        }
        //判断邮箱是否重复
        else if(userMapper.hasEmail(user.getEmail())!=null){
            res.put("error","邮箱重复");
        }
       else{
           User new_user = new User();
            String password = Md5SaltUtil.shaHex(user.getPassword(),Md5SaltUtil.generateSalt());
            new_user.setPassword(password);
            //String headPicture = "/img/profile-01.a4834deb.jpg";
            new_user.setHeadPicture(DefaultUtil.headPicture);
            new_user.setUserName(user.getUserName());
            new_user.setEmail(user.getEmail());
            new_user.setEmailNotify(DefaultUtil.emailNotify);
            new_user.setNewWordsNum(DefaultUtil.newWordsNum);
            new_user.setReviewWordsNum(DefaultUtil.reviewWordsNum);
            userMapper.insertUser(new_user);
            res.put("error","注册成功");
            res.put("code",200);
        }
        return res;
    }

    @Override
    public HashMap<String, Object> saveInformation(UserDto user) {
        HashMap<String, Object> res = new HashMap<>();
        //判断邮箱地址是否合法

        res.put("code",400);
        if(!user.getEmail().matches(RegexUtil.regex_email)){
            res.put("error","不是一个合法的邮箱地址");
        }
        else if(!user.getPhoneNumber().matches(RegexUtil.regex_phone)){
            res.put("error","不是一个合法的电话号码");
        }
        else{
            String sex = user.getSex().equals("1")?"男":"女";
            User new_user = new User();
            new_user.setSex(sex);
            new_user.setUserName(user.getUserName());
            new_user.setEmail(user.getEmail());
            new_user.setResume(user.getResume());
            new_user.setPhoneNumber(user.getPhoneNumber());
            new_user.setAddress(user.getAddress());
            new_user.setIdentity(user.getIdentity());
            try {
                userMapper.saveInformation(new_user);
                res.put("data",userMapper.selectById(user.getUserId()));
                res.put("error","更改个人信息成功");
                res.put("code",200);
            }catch (Exception e){
                res.put("error","更改个人信息失败");
            }
        }
        return res;
    }

    public HashMap<String,Object> updatePassword(UserDto user){
        HashMap<String, Object> res = new HashMap<>();
        String regex_password = ".*[a-z][1,].*";
        res.put("code",400);
        String oldPassword = Md5SaltUtil.shaHex(user.getOldPassword(),Md5SaltUtil.generateSalt());
        User user2 = userMapper.findEmailByUserId(user.getUserId());
        //判断邮箱是否正确
        if(!user.getEmail().equals(user2.getEmail())){
            res.put("error","邮箱输入有误");
        }
        //判断旧密码是否正确
        else if(!oldPassword.equals(user2.getPassword())){
            res.put("error","旧密码输入有误");
        }
        //判断两次密码输入是否一致
        else if(!user.getRePassword().equals(user.getNewPassword())){
            res.put("error","两次密码输入不一致");
        }
        else if(!user.getNewPassword().matches(regex_password)){
            res.put("error","密码必须包含数字和字母");
        }else if(user.getOldPassword().equals(user.getNewPassword())){
            res.put("error","新密码不能和旧密码一致");
        }
        else{
            User user1 = new User();
            String password = Md5SaltUtil.shaHex(user.getNewPassword(),Md5SaltUtil.generateSalt());
            user1.setUserId(user.getUserId());
            user1.setPassword(password);
            try {
                userMapper.updatePassword(user1);
                res.put("data",userMapper.selectById(user.getUserId()));
                res.put("error","修改成功！");
                res.put("code",200);
            }catch (Exception e){
                res.put("code",400);
                res.put("error","修改失败");
            }
        }
        return res;
    }

    @Override
    public HashMap<String, Object> uploadHeadPicture(User user) {
        HashMap<String, Object> res = new HashMap<>();
        try {
            userMapper.uploadHeadPicture(user);
            user = userMapper.selectById(user.getUserId());
            res.put("data",user);
            res.put("code",200);
            res.put("error","上传头像成功");
        }catch (Exception e){
            res.put("code",400);
            res.put("error","上传头像失败");
        }
        return res;
    }

    @Override
    public HashMap<String, Object> saveSetting(UserDto user) {
        HashMap<String, Object> res = new HashMap<>();
        res.put("code",400);
        User user1 = new User();
        if(user.getNewWords()==null || user.getNewWords()==false)
            user1.setNewWordsNum(0);
        else user1.setNewWordsNum(user.getNewWordsNum());
        if(user.getReviewWords()==null || user.getReviewWords()==false)
            user1.setReviewWordsNum(0);
        else user1.setReviewWordsNum(user.getReviewWordsNum());
        user1.setEmailNotify((user.getEmailNotify()==null || user.getEmailNotify()==false)?'1':'0');
        user1.setUserId(user.getUserId());
        try {
            userMapper.saveSetting(user1);
            res.put("data", userMapper.selectById(user.getUserId()));
            res.put("code",200);
            res.put("error","更改成功");
        }catch (Exception e){
            res.put("error","更改失败");
        }
        return res;
    }

    public List<Object> everyStudyRecord(Integer userId) {
        List<Object> studyRecord = new ArrayList<>();
        String year = new SimpleDateFormat("yyyy").format(new Date());
        DetailDto detailDto = new DetailDto();
        detailDto.setUserId(userId);
        detailDto.setYear(year);
        List<DetailDto> studyRecordList = studyMapper.everyStudyRecord(detailDto);
        HashMap<String,Object> res = new HashMap<>();
        List<String> month;
        List<String> days;
        for(DetailDto details:studyRecordList){
            month=new ArrayList<>();
            month.add(details.getMonth());
            days = new ArrayList<>();
            days.add(details.getDay());
            res.put("months",month);
            res.put("days",days);
            res.put("things","★"+details.getWordNum()+"个词语");
            studyRecord.add(new HashMap<>(res));
        }

        return studyRecord;
    }
}
