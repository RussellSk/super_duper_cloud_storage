package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.security.SecureRandom;
import java.util.Base64;

@Controller
@RequestMapping("/credential")
public class CredentialsController {

    private final CredentialMapper credentialMapper;
    private final UserMapper userMapper;
    private final EncryptionService encryptionService;

    public CredentialsController(CredentialMapper credentialMapper, UserMapper userMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.userMapper = userMapper;
        this.encryptionService = encryptionService;
    }

    @PostMapping("/create")
    public RedirectView createCredential(Credential credential, RedirectAttributes redirectAttributes, Principal principal) {
        try {
            User user = userMapper.getUser(principal.getName());

            if (user == null) {
                redirectAttributes.addFlashAttribute("changeError", true);
                redirectAttributes.addFlashAttribute("error", true);
                redirectAttributes.addFlashAttribute("errorMessage", "User not found");
                return new RedirectView("/credential");
            }

            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            String key = Base64.getEncoder().encodeToString(salt);

            credential.setKey(key);
            credential.setUserid(user.getUserid());
            credential.setPassword(encryptionService.encryptValue(credential.getPassword(), key));

            credentialMapper.insert(credential);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("changeError", true);
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return new RedirectView("/credential");
    }

    @GetMapping("/edit/{id}")
    public RedirectView editView(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes, Principal principal) {
        User user = userMapper.getUser(principal.getName());
        Credential currentCredential = credentialMapper.findById(id, user.getUserid());
        currentCredential.setPassword(encryptionService.decryptValue(currentCredential.getPassword(), currentCredential.getKey()));
        redirectAttributes.addFlashAttribute("editcredential", currentCredential);
        return new RedirectView("/credential");
    }

    @PostMapping("/edit/{id}")
    public RedirectView updateNote(@PathVariable("id") Integer id, Credential postedCredential, RedirectAttributes redirectAttributes, Principal principal) {
        try {
            User user = userMapper.getUser(principal.getName());
            Credential currentCredential = credentialMapper.findById(id, user.getUserid());

            postedCredential.setCredentialid(id);
            postedCredential.setUserid(user.getUserid());
            postedCredential.setPassword(encryptionService.encryptValue(postedCredential.getPassword(), currentCredential.getKey()));
            credentialMapper.update(postedCredential);

            redirectAttributes.addFlashAttribute("success", true);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("changeError", true);
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return new  RedirectView("/credential");
    }

    @GetMapping()
    public String credentialView(Model model, Principal principal) {
        User user = userMapper.getUser(principal.getName());
        model.addAttribute("credentials", credentialMapper.credentials(user.getUserid()));
        return "credential";
    }

    @PostMapping("/delete/{id}")
    public RedirectView deleteNote(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes, Principal principal) {
        try {
            User user = userMapper.getUser(principal.getName());
            credentialMapper.delete(id, user.getUserid());
            redirectAttributes.addFlashAttribute("success", true);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return new RedirectView("/credential");
    }

}
