package com.guwen.mapper;
import com.guwen.model.Dto.UserDto;
import com.guwen.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Mapper
public interface UserMapper extends BaseMapper<User>{
    User login(String email, String password);

    String hasEmail(String userName);

    void insertUser(User user);

    void saveInformation(User user);

    User findEmailByUserId(Integer userId);

    void updatePassword(User user);

    void uploadHeadPicture(User user);

    void saveSetting(User user1);
}
