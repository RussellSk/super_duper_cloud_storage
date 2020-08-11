package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class DashboardController {

    private final NoteMapper noteMapper;
    private final UserMapper userMapper;
    private final CredentialMapper credentialMapper;
    private final FileMapper fileMapper;

    public DashboardController(NoteMapper noteMapper, UserMapper userMapper, CredentialMapper credentialMapper, FileMapper fileMapper) {
        this.noteMapper = noteMapper;
        this.userMapper = userMapper;
        this.credentialMapper = credentialMapper;
        this.fileMapper = fileMapper;
    }

    @RequestMapping(value = {"/dashboard", "/"})
    public String dashboardView(Model model, Principal principal) {
        User user = userMapper.getUser(principal.getName());
        model.addAttribute("totalNotes", noteMapper.countNotes(user.getUserid()));
        model.addAttribute("totalUsers", userMapper.countUsers());
        model.addAttribute("totalCredentials", credentialMapper.countCredential(user.getUserid()));
        model.addAttribute("totalFiles", fileMapper.countFiles(user.getUserid()));
        return "dashboard";
    }
}
