package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM USERS WHERE username = #{username}")
    public User getUser(String username);

    @Select("SELECT * FROM USERS")
    public ArrayList<User> getUsers();

    @Insert("INSERT INTO USERS (username, salt, password, firstname, lastname) VALUES (#{username}, #{salt}, #{password}, #{firstname}, #{lastname})")
    @Options(useGeneratedKeys = true, keyProperty = "userid")
    public int insert(User user);

    @Select("SELECT COUNT(*) FROM USERS")
    public int countUsers();

    @Delete("DELETE FROM USERS WHERE userid = #{userid}")
    public int delete(Integer userid);
}
