package com.sleepy.zeo.springboot.database.mybatis.pojo;

import lombok.Data;

@Data
public class SysUser {

    public int userId;
    public String username;
    public String password;
}
