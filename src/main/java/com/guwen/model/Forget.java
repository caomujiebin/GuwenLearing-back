package com.guwen.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("forget")
public class Forget {
    private Integer userId;
    private Integer wordId;
    private Integer forgetNum;//遗忘次数
    private Integer forgetStatus;//遗忘状态 1：遗忘 0：未遗忘
}
