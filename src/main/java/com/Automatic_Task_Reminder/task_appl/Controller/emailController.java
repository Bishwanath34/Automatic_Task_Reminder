package com.Automatic_Task_Reminder.task_appl.Controller;

import com.Automatic_Task_Reminder.task_appl.DTO.MailDto;
import com.Automatic_Task_Reminder.task_appl.Service.EmailServiceClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class emailController{
    @Autowired
    private EmailServiceClass emailServiceClass;
    @PostMapping("/sendmail")
    public String sendSimpleMail(@RequestBody MailDto dto){
        return emailServiceClass.sendSimpleMail(dto);
    }
    @PostMapping("/sendmail-attachment")
    public String sendSimpleMailWithAttachment(@RequestBody MailDto dto){
        return emailServiceClass.sendSimpleMailWithAttachment(dto);
    }
}
