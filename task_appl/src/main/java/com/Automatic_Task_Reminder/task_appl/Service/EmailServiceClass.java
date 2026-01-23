package com.Automatic_Task_Reminder.task_appl.Service;

import com.Automatic_Task_Reminder.task_appl.DTO.MailDto;
import com.Automatic_Task_Reminder.task_appl.Entity.User;
import com.Automatic_Task_Reminder.task_appl.Entity.taskModel;
import com.Automatic_Task_Reminder.task_appl.Repository.TaskRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.Negative;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class EmailServiceClass{
    @Autowired
    private TaskRepository taskRepository;
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

    public String sendSimpleMailWithAttachment(User user) {
        try {
            // 1. Fetch tasks for the given user

            List<taskModel> tasks= taskRepository.findByUserId(user.getId());

            // 2. Generate CSV in memory
            StringWriter sw = new StringWriter();
            CSVPrinter printer = new CSVPrinter(sw, CSVFormat.DEFAULT.withHeader(
                    "ID", "Title", "User", "Description", "DueDate", "Status", "Priority", "CreatedAt", "CompletedAt", "ReminderSent"
            ));

            for (taskModel task : tasks) {
                printer.printRecord(
                        task.getId(),
                        task.getTitle(),
                        task.getUser().getName(),
                        task.getDescription(),
                        task.getDueDate(),
                        task.getStatus(),
                        task.getPriority(),
                        task.getCreatedAt(),
                        task.getCompletedAt(),
                        task.isReminderSent()
                );
            }
            printer.flush();
            MailDto mailDto = new MailDto.Builder()
                    .sendTo(user.getEmail())   // assuming User has getEmail()
                    .subject("Your Tasks CSV")       // email subject
                    .text("Please find attached your tasks as CSV.") // email body
                    // .attachment("path/to/file.csv") // not needed if using in-memory CSV
                    .build();
            // 3. Convert CSV to in-memory attachment
            byte[] csvBytes = sw.toString().getBytes(StandardCharsets.UTF_8);
            ByteArrayResource attachment = new ByteArrayResource(csvBytes);

            // 4. Prepare MimeMessage
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(mailDto.getSendTo());
            helper.setFrom(sender);
            helper.setText(mailDto.getText());
            helper.setSubject(mailDto.getSubject());

            // 5. Attach CSV directly from memory
            helper.addAttachment("tasks.csv", attachment);

            // 6. Send email
            mailSender.send(mimeMessage);

            return "Message sent successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send mail";
        }
    }
}
