package com.guwen.service;

import com.guwen.model.Dto.UserDto;
import com.guwen.model.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;

public interface UserService {
    HashMap<String, Object> login(User user);

    HashMap<String, Object> signUp(UserDto user);

    HashMap<String, Object> saveInformation(UserDto user);

    HashMap<String, Object> updatePassword(UserDto user);

    HashMap<String, Object> uploadHeadPicture(User user);

    HashMap<String, Object> saveSetting(UserDto user);
}
