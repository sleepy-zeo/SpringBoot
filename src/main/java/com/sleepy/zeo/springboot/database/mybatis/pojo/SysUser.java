package com.sleepy.zeo.springboot.database.mybatis.pojo;

import lombok.Data;

@Data
public class SysUser {

    private int userId;
    private String username;
    private String password;
}
