package com.Automatic_Task_Reminder.task_appl.ControllerAdvice;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ModelAttribute
    public void addLoggedUserToModel(HttpSession session, Model model) {
        model.addAttribute("loggedUser", session.getAttribute("loggedUser"));
    }
}
