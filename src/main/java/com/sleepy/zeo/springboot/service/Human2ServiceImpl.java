package com.sleepy.zeo.springboot.service;

import org.springframework.stereotype.Service;

@Service(value = "doctorService")
public class Human2ServiceImpl implements HumanService {

    @Override
    public String name() {
        System.out.println("doctor");
        return "doctor";
    }
}
