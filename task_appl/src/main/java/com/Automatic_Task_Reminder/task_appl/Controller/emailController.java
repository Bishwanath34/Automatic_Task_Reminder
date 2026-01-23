package com.Automatic_Task_Reminder.task_appl.Controller;

import com.Automatic_Task_Reminder.task_appl.DTO.MailDto;
import com.Automatic_Task_Reminder.task_appl.Entity.User;
import com.Automatic_Task_Reminder.task_appl.Service.EmailServiceClass;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/email")
public class emailController{
    @Autowired
    private EmailServiceClass emailServiceClass;
    @PostMapping("/sendmail")
    public String sendSimpleMail(@RequestBody MailDto dto){
        return emailServiceClass.sendSimpleMail(dto);
    }
    @GetMapping("/csv-send")
    public String sendSimpleMailWithAttachment(HttpSession session){
        User loggedUser = (User) session.getAttribute("loggedUser");
        emailServiceClass.sendSimpleMailWithAttachment(loggedUser );
        return "redirect:/api/tasks?pageNo=0&pageSize=5";
    }
}
