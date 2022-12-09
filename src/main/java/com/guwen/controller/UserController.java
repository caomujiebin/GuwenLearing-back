package com.guwen.controller;

import com.guwen.model.Dto.UserDto;
import com.guwen.model.User;
import com.guwen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@CrossOrigin
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    //登录
    @PostMapping({"/user/login"})
    public HashMap<String,Object> login(@RequestBody User user) {
        HashMap<String, Object> res = userService.login(user);
        return res;
    }

    //注册
    @PostMapping({"/user/signUp"})
    public HashMap<String,Object> signUp(@RequestBody UserDto user) {
        HashMap<String, Object> res = userService.signUp(user);
        return res;
    }

    //更改用户信息
    @PostMapping("/user/saveInformation")
    public HashMap<String,Object> saveInformation(@RequestBody UserDto user) {
        HashMap<String, Object> res = userService.saveInformation(user);
        return res;
    }

    //更改用户信息
    @PostMapping("/user/updatePassword")
    public HashMap<String,Object> updatePassword(@RequestBody UserDto user) {
        HashMap<String, Object> res = userService.updatePassword(user);
        return res;
    }

    //上传头像
    @RequestMapping("/user/uploadHeadPicture")
    public HashMap<String,Object> uploadHeadPicture(@RequestBody User user) {
        HashMap<String, Object> res = userService.uploadHeadPicture(user);
        return res;
    }

    //系统设置
    @RequestMapping("/user/saveSetting")
    public HashMap<String,Object> saveSetting(@RequestBody UserDto user) {
        HashMap<String, Object> res = userService.saveSetting(user);
        return res;
    }
}