package com.Automatic_Task_Reminder.task_appl.Controller;

import com.Automatic_Task_Reminder.task_appl.Entity.User;
import com.Automatic_Task_Reminder.task_appl.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class userController {

    @Autowired
    private UserService userService;


    @GetMapping("/")
    public String landingPage() {
        return "landing";
    }


    @GetMapping("/regisForm")
    public String regisForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user, Model model) {
        try {
            User savedUser = userService.register(user);
            model.addAttribute("Id", savedUser.getId());
            return "verify"; // go to OTP page
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }


    @GetMapping("/loginForm")
    public String loginForm(Model model) {
        model.addAttribute("logUser", new User());
        return "login";
    }


    @PostMapping("/login")
    public String login(@ModelAttribute("logUser") User user, HttpSession session, Model model) {
        try {
            User loggedUser = userService.login(user.getEmail(), user.getPassword(), session);
            session.setAttribute("loggedUser", loggedUser);
            return "redirect:/api/tasks?pageNo=0&pageSize=5";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }


    @PostMapping("/verify-otp/{id}")
    public String verifyOtp(
            @RequestParam String d1,
            @RequestParam String d2,
            @RequestParam String d3,
            @RequestParam String d4,
            @RequestParam String d5,
            @RequestParam String d6,
            @PathVariable Integer id,
            Model model
    ) {
        String enteredOtp = d1 + d2 + d3 + d4 + d5 + d6;
        try {
            boolean valid = userService.validateOtp(enteredOtp, id);
            if (valid) return "redirect:/loginForm";
            else {
                model.addAttribute("error", "Invalid OTP. Please try again.");
                model.addAttribute("Id", id);
                return "verify";
            }
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("Id", id);
            return "verify";
        }
    }


    @GetMapping("/resend-otp/{id}")
    public String resendOtp(@PathVariable Integer id, Model model) {
        try {
            userService.resendOtp(id);
            model.addAttribute("message", "A new OTP has been sent to your email.");
            model.addAttribute("Id", id);
            return "verify";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("Id", id);
            return "verify";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpSession session, Model model) {
        try {
            session.invalidate();
            model.addAttribute("message", "Logged out successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Error during logout. Please try again.");
        }
        return "landing";
    }
}
