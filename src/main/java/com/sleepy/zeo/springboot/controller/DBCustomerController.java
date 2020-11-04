package com.sleepy.zeo.springboot.controller;

import com.sleepy.zeo.springboot.database.mybatis.dao.CustomerDao;
import com.sleepy.zeo.springboot.database.mybatis.pojo.CustomerBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/db")
public class DBCustomerController {

    private CustomerDao customerDao;

    @Autowired
    public void setCustomerDao(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @RequestMapping("/info/{id}")
    @ResponseBody
    public CustomerBean getUsers(@PathVariable("id") int id) throws Exception {
        return customerDao.fetchCustomer(id);
    }

}
