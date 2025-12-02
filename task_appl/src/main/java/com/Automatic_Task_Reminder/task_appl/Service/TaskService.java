package com.Automatic_Task_Reminder.task_appl.Service;

import com.Automatic_Task_Reminder.task_appl.Entity.taskModel;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    private List<taskModel> taskList = new ArrayList<>();

    public TaskService() {

        taskList.add(new taskModel(
                "Pay Electricity Bill",
                "Pay the monthly electricity bill before due date",
                "Bills",
                "High",
                "Pending",
                "false",
                null,
                LocalDate.of(2025, 1, 20),
                LocalTime.of(18, 0),
                LocalDate.of(2025, 1, 19),
                LocalDateTime.now(),
                LocalDateTime.now(),
                "user123"
        ));
    }

    public List<taskModel> getTasks() {

        return taskList;
    }

    public void addTask(taskModel taskModel1) {
        taskList.add(taskModel1);
    }
}
