package com.Automatic_Task_Reminder.task_appl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TaskApplApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskApplApplication.class, args);
	}

}
