package com.guwen.model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")
public class User {
    private Integer userId;
    private String email;
    private String password;
    private String userName;
    private String resume;
    private String sex;
    private String phoneNumber;
    private String address;
    private String identity;
    private String headPicture;
    private String coverPhoto;
    private char emailNotify;
    private Integer newWordsNum;
    private Integer reviewWordsNum;
}
