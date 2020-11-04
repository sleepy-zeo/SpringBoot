package com.sleepy.zeo.springboot.controller;

import com.sleepy.zeo.springboot.database.redis.pojo.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/redis")
public class DBRedisController {

    private RedisTemplate redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @RequestMapping("/put")
    @ResponseBody
    public String put() {
        Customer customer = new Customer();
        customer.setId(1);
        customer.setName("小明");
        customer.setGender("男");
        customer.setTelephone("132444455555");
        //调用Redis的API存入数据
        redisTemplate.opsForValue().set("customer", customer);
        return "success";
    }

    @RequestMapping("/get")
    @ResponseBody
    public Customer get() {
        Customer customer = (Customer) redisTemplate.opsForValue().get("customer");
        return customer;
    }
}
