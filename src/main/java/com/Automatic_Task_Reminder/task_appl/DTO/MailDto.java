package com.Automatic_Task_Reminder.task_appl.DTO;

public class MailDto {

    private String sendTo;
    private String text;
    private String subject;
    private String attachment;

    // ✅ Private constructor (forces use of Builder)
    private MailDto(Builder builder) {
        this.sendTo = builder.sendTo;
        this.text = builder.text;
        this.subject = builder.subject;
        this.attachment = builder.attachment;
    }

    // ✅ Getters
    public String getSendTo() {
        return sendTo;
    }

    public String getText() {
        return text;
    }

    public String getSubject() {
        return subject;
    }

    public String getAttachment() {
        return attachment;
    }

    // ✅ Builder static inner class
    public static class Builder {
        private String sendTo;
        private String text;
        private String subject;
        private String attachment;

        public Builder sendTo(String sendTo) {
            this.sendTo = sendTo;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder attachment(String attachment) {
            this.attachment = attachment;
            return this;
        }

        public MailDto build() {
            return new MailDto(this);
        }
    }

    @Override
    public String toString() {
        return "MailDto{" +
                "sendTo='" + sendTo + '\'' +
                ", text='" + text + '\'' +
                ", subject='" + subject + '\'' +
                ", attachment='" + attachment + '\'' +
                '}';
    }
}
