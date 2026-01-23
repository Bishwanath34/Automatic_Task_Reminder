package com.Automatic_Task_Reminder.task_appl.Scheduler;

import com.Automatic_Task_Reminder.task_appl.DTO.MailDto;
import com.Automatic_Task_Reminder.task_appl.Entity.taskModel;
import com.Automatic_Task_Reminder.task_appl.Repository.TaskRepository;
import com.Automatic_Task_Reminder.task_appl.Service.EmailServiceClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@EnableScheduling
public class SchedulerService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private EmailServiceClass emailServiceClass;
    @Scheduled(cron = "0 0 9 * * ?", zone = "Asia/Kolkata")
    public void sendTomorrowTaskReminder(){
        LocalDate tomorrow=LocalDate.now().plusDays(1);
        List<taskModel> tasks=taskRepository.findTasksDueTomorrow(tomorrow);
        for(taskModel task:tasks){

            MailDto mail = new MailDto.Builder()
                    .sendTo(task.getUser().getEmail())
                    .subject("⏰ Task Due Tomorrow")
                    .text("""
                          Hi %s,

                          This is a reminder that your task:
                          "%s"

                          is due tomorrow (%s).

                          — Task Reminder App
                          """.formatted(
                            task.getUser().getName(),
                            task.getTitle(),
                            task.getDueDate()))
                    .build();
            emailServiceClass.sendSimpleMail(mail);

            // Mark reminder as sent
            task.setReminderSent(true);
            taskRepository.save(task);
        }
    }
}
