package com.sleepy.zeo.springboot.service;

import org.springframework.stereotype.Service;

@Service(value = "teacherService")
public class Human1ServiceImpl implements HumanService {
    @Override
    public String name() {
        System.out.println("teacher");
        return "teacher";
    }
}
