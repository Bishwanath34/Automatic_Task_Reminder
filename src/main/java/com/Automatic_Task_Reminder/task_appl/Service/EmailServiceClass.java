package com.Automatic_Task_Reminder.task_appl.Service;

import com.Automatic_Task_Reminder.task_appl.DTO.MailDto;
import com.Automatic_Task_Reminder.task_appl.Entity.taskModel;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailServiceClass{
    @Autowired
    private JavaMailSender mailSender;
@Value("${spring.mail.username}")
private String sender;
    public String sendSimpleMail(MailDto dto){
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(sender);
            simpleMailMessage.setTo(dto.getSendTo());
            simpleMailMessage.setSubject(dto.getSubject());
            simpleMailMessage.setText(dto.getText());
            mailSender.send(simpleMailMessage);
            return "success";
        }catch (RuntimeException e){
return "error while sending the mail";
        }
    }

    public String sendSimpleMailWithAttachment(MailDto dto) {
        MimeMessage mimeMessage=mailSender.createMimeMessage();
        MimeMessageHelper messageHelper;
        try{
            messageHelper=new MimeMessageHelper(mimeMessage,true);
            messageHelper.setTo(dto.getSendTo());
            messageHelper.setFrom(sender);
            messageHelper.setText(dto.getText());
            messageHelper.setSubject(dto.getSubject());
            FileSystemResource fileSystemResource=new FileSystemResource(new File(dto.getAttachment()));
            messageHelper.addAttachment(fileSystemResource.getFilename(),fileSystemResource);
            mailSender.send(mimeMessage);
return "message send successfully";
        } catch (MessagingException e) {
            return "fail to send mail";
        }
    }
}
