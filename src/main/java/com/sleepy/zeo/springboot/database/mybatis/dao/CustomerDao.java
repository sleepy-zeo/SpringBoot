package com.sleepy.zeo.springboot.database.mybatis.dao;

import com.sleepy.zeo.springboot.database.mybatis.pojo.CustomerBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CustomerDao {
    CustomerBean fetchCustomer(@Param("id") int id) throws Exception;

    List<CustomerBean> fetchCustomers(@Param("grade") String grade, @Param("age") int age);

    List<CustomerBean> fetchAllCustomers();

    void insert(CustomerBean customer);

    void update(CustomerBean customer);

    void delete(@Param("id") int id);
}
