package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

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

    @PostMapping("/delete/{id}")
    public RedirectView deleteUser(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            userMapper.delete(id);
            redirectAttributes.addFlashAttribute("success", true);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return new RedirectView("/user");
    }
}
