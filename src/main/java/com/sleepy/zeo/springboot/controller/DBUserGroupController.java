package com.sleepy.zeo.springboot.controller;

import com.sleepy.zeo.springboot.database.mybatis.dao.UserGroupDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/ug")
public class DBUserGroupController {
    private UserGroupDao userGroup;

    @Autowired
    public void setUserGroup(UserGroupDao userGroup) {
        this.userGroup = userGroup;
    }

    @RequestMapping(value = "/insert/{type}", method = RequestMethod.POST)
    @ResponseBody
    public String insert(@PathVariable("type") int insertType, HttpServletRequest request) {
        if (insertType == 1) {
            String userId = request.getParameter("userId");
            String name = request.getParameter("name");
            String telephone = request.getParameter("telephone");
            boolean tokenExpired = Boolean.parseBoolean(request.getParameter("tokenExpired"));
            boolean deleteFlag = Boolean.parseBoolean(request.getParameter("deleteFlag"));
            userGroup.insertUser(userId, name, telephone, tokenExpired, deleteFlag);
        } else if (insertType == 2) {
            String groupId = request.getParameter("groupId");
            String name = request.getParameter("name");
            String type = request.getParameter("type");
            String parentId = request.getParameter("parentId");
            userGroup.insertGroup(groupId, name, type, parentId);
        } else {
            String userId = request.getParameter("userId");
            String groupId = request.getParameter("groupId");
            userGroup.insertUserGroup(userId, groupId);
        }
        return "success";
    }

    @RequestMapping("/fetch")
    @ResponseBody
    public List<UserGroupDao.GroupCollection> fetch() {
        return userGroup.fetchGroupWithUsers();
    }

}
