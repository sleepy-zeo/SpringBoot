package com.sleepy.zeo.springboot.database.mybatis.pojo;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class SysPermission {

    public int id;
    public String url;
    public int roleId;
    private String permission;

    public List<Object> getPermissions() {
        String data = this.permission.replace(" ", "").trim();
        return Arrays.asList(data.split(","));
    }

}
