package com.sleepy.zeo.springboot.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "employee")
public class Employee {

    private String username;
    private int age;
    private List<String> lang;
    private Map<String, String> websites;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setLang(List<String> lang) {
        this.lang = lang;
    }

    public void setWebsites(Map<String, String> websites) {
        this.websites = websites;
    }

    @Override
    public String toString() {
        return "Employee[" +
                "username='" + username + '\'' +
                ", age=" + age +
                ", lang=" + lang +
                ", websites=" + websites +
                ']';
    }
}
