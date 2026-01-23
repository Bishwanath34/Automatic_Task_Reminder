package com.Automatic_Task_Reminder.task_appl.enums;

public enum StatusEnum {
    DONE, IN_PROGRESS, PENDING;

    public String getDisplayName() {
        switch(this) {
            case DONE: return "Done";
            case IN_PROGRESS: return "In Progress";
            case PENDING: return "Pending";
            default: return "";
        }
    }

    public String getCssClass() {
        switch(this) {
            case DONE: return "done";
            case IN_PROGRESS: return "in-progress";
            case PENDING: return "pending";
            default: return "";
        }
    }
}
