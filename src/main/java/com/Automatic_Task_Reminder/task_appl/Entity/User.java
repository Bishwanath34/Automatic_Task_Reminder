package com.Automatic_Task_Reminder.task_appl.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;

import java.time.LocalDateTime;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    public LocalDateTime getOtpExpiryTime() {
        return otpExpiryTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", otpExpiryTime=" + otpExpiryTime +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", verified=" + verified +
                ", createdAt=" + createdAt +
                ", otp='" + otp + '\'' +
                '}';
    }

    public void setOtpExpiryTime(LocalDateTime otpExpiryTime) {
        this.otpExpiryTime = otpExpiryTime;
    }

    private LocalDateTime otpExpiryTime;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    @Email
    private String email;
    private String password;
    private boolean verified=false;
    private LocalDateTime createdAt;
    private String otp;
    public User(){
    }
    public User(String name, String email, String password, boolean verified, LocalDateTime createdAt){
        this.name=name;
        this.email=email;
        this.password=password;
this.createdAt=createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
