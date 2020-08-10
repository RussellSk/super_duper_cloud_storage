package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserMapper userMapper;

    public UserController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @GetMapping()
    public String getUsers(Model model) {
        model.addAttribute("users", userMapper.getUsers());
        return "user";
    }

    @PostMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable Integer id, Model model) {
        userMapper.delete(id);
        model.addAttribute("users", userMapper.getUsers());
        model.addAttribute("success", true);
        return "user";
    }
}
