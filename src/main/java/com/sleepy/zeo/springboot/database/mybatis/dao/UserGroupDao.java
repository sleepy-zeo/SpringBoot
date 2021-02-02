package com.sleepy.zeo.springboot.database.mybatis.dao;

import lombok.Data;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/*
 * // @Results注解
 * @Documented
 * @Retention(RetentionPolicy.RUNTIME)
 * @Target({ElementType.METHOD})
 * public @interface Results {
 *     String id() default ""; // Results注解的唯一标识
 *     Result[] value() default {}; // Result[]数组
 * }
 *
 * // @Result注解
 * @Documented
 * @Retention(RetentionPolicy.RUNTIME)
 * @Target({})
 * public @interface Result {
 *     boolean id() default false; // Result注解的唯一标识
 *     String column() default ""; // 对应数据库中的列名
 *     String property() default ""; // 对应类中的属性名
 *     Class<?> javaType() default void.class; // 数据类型
 *     JdbcType jdbcType() default JdbcType.UNDEFINED; // jdbc类型
 *     Class<? extends TypeHandler> typeHandler() default UnknownTypeHandler.class;
 *     One one() default @One; // 一对一配置
 *     Many many() default @Many; // 一对多配置
 * }
 *
 * // @Many关联查询
 * @Documented
 * @Retention(RetentionPolicy.RUNTIME)
 * @Target({})
 * public @interface Many {
 *     String select() default "";  // 选择要执行的方法
 *     FetchType fetchType() default FetchType.DEFAULT;  //枚举值，懒加载、及时加载、默认
 * }
 *
 * // @ResultMap
 * @Documented
 * @Retention(RetentionPolicy.RUNTIME)
 * @Target({ElementType.METHOD})
 * public @interface ResultMap {
 *     String[] value(); // 指定要引用的@Results，value值为@Results的id值
 * }
 */

/**
 *
 * create table tb_user(
 * user_id varchar(32) not null,
 * name varchar(32) not null,
 * telephone varchar(11),
 * token_expired int(1) default 0,
 * delete_flag int(1) default 0,
 * primary key(user_id)
 * );
 *
 * create table tb_group(
 * group_id varchar(32) not null,
 * name varchar(32) not null,
 * type varchar(8),
 * parent_id varchar(32),
 * primary key(group_id)
 * );
 *
 * create table tb_user_group(
 * id int not null primary key auto_increment,
 * user_id varchar(32) not null,
 * group_id varchar(32) not null,
 * constraint fk_user_group_user_id foreign key(user_id) references tb_user(user_id) ON UPDATE CASCADE,
 * constraint fk_user_group_group_id foreign key(group_id) references tb_group(group_id) ON UPDATE CASCADE
 * );
 *
 */

@Mapper
@Component
public interface UserGroupDao {

    /**
     * Data注解会自动添加 set get hashCode equals toString等方法
     */
    @Data
    class User {
        String userId;
        String name;
        String telephone;
        boolean tokenExpired;
        boolean deleteFlag;
    }

    @Data
    class Group {
        String groupId;
        String name;
        String type;
        String parentId;
    }

    @Data
    class GroupCollection {
        String groupId;
        String name;
        String type;
        String parentId;
        List<User> userList;
    }

    @Results(
            id = "user_map",
            value = {
                    @Result(property = "userId", column = "user_id", id = true),
                    @Result(property = "name", column = "name"),
                    @Result(property = "telephone", column = "telephone"),
                    @Result(property = "tokenExpired", column = "token_expired"),
                    @Result(property = "deleteFlag", column = "delete_flag")
            }
    )
    @Select({"select * from tb_user where name=#{name}"})
    User fetchUser(@Param("name") String userName);

    @ResultMap(value = "user_map")
    @Select({"select * from tb_user"})
    List<User> fetchUsers();

    @Insert({"insert into tb_user values(#{id},#{name},#{tel},#{tokenExpired},#{deleteFlag})"})
    int insertUser(@Param("id") String id,
                   @Param("name") String name,
                   @Param("tel") String telephone,
                   @Param("tokenExpired") boolean tokenExpired,
                   @Param("deleteFlag") boolean deleteFlag);

    @Insert({"update tb_user set user_id=#{id},name=#{name},telephone=#{tel},token_expired=#{tokenExpired},delete_flag=#{deleteFlag}"})
    int updateUser(@Param("id") String id,
                   @Param("name") String name,
                   @Param("tel") String telephone,
                   @Param("tokenExpired") boolean tokenExpired,
                   @Param("deleteFlag") boolean deleteFlag);

    @Delete({"delete from tb_user where user_id=#{id}"})
    int deleteUser(@Param("id") String id);

    @Insert({"insert into tb_group values(#{id},#{name},#{type},#{parentId})"})
    int insertGroup(@Param("id") String id,
                    @Param("name") String name,
                    @Param("type") String type,
                    @Param("parentId") String parentId);

    @Insert({"insert into tb_user_group(user_id,group_id) values(#{userId},#{groupId})"})
    int insertUserGroup(@Param("userId") String userId,
                        @Param("groupId") String groupId);

    @Results({
            @Result(property = "groupId", column = "group_id", id = true),
            @Result(property = "name", column = "name"),
            @Result(property = "type", column = "type"),
            @Result(property = "parentId", column = "parent_id"),
            @Result(property = "userList", javaType = List.class, many = @Many(select = "fetchUsersByGroupId"), column = "group_id")
    })
    @Select({"select * from tb_group"})
    List<GroupCollection> fetchGroupWithUsers();

    //
    // 这里group_id的值会传到fetchUsersByGroupId中作为参数
    //
    // 如果要传递对象，则column = "{groupId=group_id, userName=user_name}"
    // fetchUsersByGroupId的参数为Map<String,Object>，select语句中则是#{groupId} #{userName}
    //
    @ResultMap(value = "user_map")
    @Select({"select u.* from tb_user u",
            "inner join tb_user_group ug on u.user_id = ug.user_id",
            "where ug.group_id=#{groupId}"
    })
    List<User> fetchUsersByGroupId(@Param("groupId") String groupId);

}
