package com.example.dccworkflow.controller;

import com.example.dccworkflow.entity.User;
import com.example.dccworkflow.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/changePwdPage")
    public String changePwdPage() {
        return "user/changePwd";
    }

    @PostMapping("changePwd")
    public RedirectView changePwd(@RequestParam String oldPassword,
                                  @RequestParam String newPassword) {
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        try {
            userService.changePwd(user.getId(), oldPassword, newPassword);
        } catch (Exception e) {
            return new RedirectView("/user/changePwdPage?errOldPwd");
        }
        return new RedirectView("/user/changePwdPage?success");
    }
}
