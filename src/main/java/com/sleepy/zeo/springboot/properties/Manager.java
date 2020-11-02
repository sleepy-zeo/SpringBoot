package com.sleepy.zeo.springboot.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class Manager {

    @Value("${manager.username}")
    private String name;
    @Value("${manager.age}")
    private int age;
    @Value("#{'${manager.lang}'.split(',')}")
    private List<String> languages;
    @Value("#{${manager.websites}}")
    private Map<String, String> websites;

    @Override
    public String toString() {
        return "Manager[" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", languages=" + languages +
                ", websites=" + websites +
                ']';
    }
}
