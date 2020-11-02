package com.sleepy.zeo.springboot.controller;

import com.sleepy.zeo.springboot.properties.Employee;
import com.sleepy.zeo.springboot.properties.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/configuration")
public class PropertiesController {

    private Manager manager;
    private Employee employee;

    @Autowired
    public void setManager(Manager manager) {
        this.manager = manager;
    }

    @Autowired
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @RequestMapping("/display")
    @ResponseBody
    public String display() {
        return manager.toString() + employee.toString();
    }

}
