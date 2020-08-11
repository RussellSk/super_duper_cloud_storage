package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("/file")
public class FileController {

    private final UserMapper userMapper;
    private final FileMapper fileMapper;

    public FileController(UserMapper userMapper, FileMapper fileMapper) {
        this.userMapper = userMapper;
        this.fileMapper = fileMapper;
    }

    @GetMapping()
    public String filesView(Model model, Principal principal) {
        User user = userMapper.getUser(principal.getName());
        model.addAttribute("files", fileMapper.files(user.getUserid()));
        return "file";
    }

    @GetMapping("/download/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> downloadFile(@PathVariable Integer id, Principal principal) {
        User user = userMapper.getUser(principal.getName());
        File file = fileMapper.findById(id, user.getUserid());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,	"attachment; filename=\"" + file.getFilename() + "\"").body(file.getFiledata());
    }

    @PostMapping("/upload")
    public RedirectView uploadFile(@RequestParam("fileUpload")MultipartFile uploadingFile, RedirectAttributes redirectAttributes, Principal principal) {
        try {

            if (uploadingFile.isEmpty()) {
                throw new Exception("File input is empty");
            }

            User user = userMapper.getUser(principal.getName());

            if (fileMapper.findByFilename(uploadingFile.getOriginalFilename(), user.getUserid()) != null) {
                throw new Exception("User: " + user.getUsername() + " can not upload the same file (" + uploadingFile.getOriginalFilename() + ") twice. Please select another file and try again.");
            }

            File file = new File();
            file.setFiledata(uploadingFile.getBytes());
            file.setContenttype(uploadingFile.getContentType());
            file.setFilename(uploadingFile.getOriginalFilename());
            file.setUserid(user.getUserid());
            file.setFilesize(Long.toString(uploadingFile.getSize()));
            fileMapper.create(file);

            redirectAttributes.addFlashAttribute("success", true);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return new RedirectView("/file");
    }

    @PostMapping("/delete/{id}")
    public RedirectView deleteFile(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes, Principal principal) {
        try {
            User user = userMapper.getUser(principal.getName());
            fileMapper.delete(id, user.getUserid());
            redirectAttributes.addFlashAttribute("success", true);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return new RedirectView("/file");
    }
}
