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
    public String filesView(Model model) {
        model.addAttribute("files", fileMapper.files());
        return "file";
    }

    @GetMapping("/download/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> downloadFile(@PathVariable Integer id) {
        File file = fileMapper.findById(id);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,	"attachment; filename=\"" + file.getFilename() + "\"").body(file.getFiledata());

    }

    @PostMapping("/upload")
    public RedirectView uploadFile(@RequestParam("fileUpload")MultipartFile uploadingFile, RedirectAttributes redirectAttributes, Principal principal) {
        try {
            User user = userMapper.getUser(principal.getName());

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
    public RedirectView deleteFile(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            fileMapper.delete(id);
            redirectAttributes.addFlashAttribute("success", true);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return new RedirectView("/file");
    }
}
