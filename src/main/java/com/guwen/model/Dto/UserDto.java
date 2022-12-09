package com.guwen.model.Dto;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;

@Data
public class UserDto {
    private Integer userId;
    private String email;
    private String oldPassword;
    private String newPassword;
    private String rePassword;
    private String password;
    private String userName;
    private String phoneNumber;
    private String sex;
    private String resume;
    private String address;
    private String identity;
    private Boolean newWords;
    private Boolean emailNotify;
    private Boolean reviewWords;
    private Integer newWordsNum;
    private Integer reviewWordsNum;
}
