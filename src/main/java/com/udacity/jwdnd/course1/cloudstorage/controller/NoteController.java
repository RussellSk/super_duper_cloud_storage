package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.websocket.server.PathParam;
import java.security.Principal;

@Controller
@RequestMapping("/note")
public class NoteController {

    private final NoteMapper noteMapper;
    private final UserMapper userMapper;

    public NoteController(NoteMapper noteMapper, UserMapper userMapper) {
        this.noteMapper = noteMapper;
        this.userMapper = userMapper;
    }

    @GetMapping()
    public String getNotes(Model model) {
        model.addAttribute("notes", noteMapper.getNotes());
        return "note";
    }

    @PostMapping("/create")
    public RedirectView createNote(Note postedNote, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            User user = userMapper.getUser(principal.getName());

            if (user == null) {
                redirectAttributes.addFlashAttribute("changeError", true);
                redirectAttributes.addFlashAttribute("error", true);
                redirectAttributes.addFlashAttribute("errorMessage", "User not found");
                return new RedirectView("/note");
            }

            postedNote.setUserid(user.getUserid());
            noteMapper.insert(postedNote);
            redirectAttributes.addFlashAttribute("success", true);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("changeError", true);
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return new RedirectView("/note");
    }

    @GetMapping("/edit/{id}")
    public RedirectView editView(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("editnote", noteMapper.getNote(id));
        return new RedirectView("/note");
    }

    @PostMapping("/edit/{id}")
    public RedirectView updateNote(@PathVariable("id") Integer id, Note postedNote, RedirectAttributes redirectAttributes) {
        try {
            postedNote.setNoteid(id);
            noteMapper.update(postedNote);
            redirectAttributes.addFlashAttribute("success", true);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("changeError", true);
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return new  RedirectView("/note");
    }

    @PostMapping("/delete/{id}")
    public RedirectView deleteNote(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            noteMapper.delete(id);
            redirectAttributes.addFlashAttribute("success", true);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return new RedirectView("/note");
    }
}
