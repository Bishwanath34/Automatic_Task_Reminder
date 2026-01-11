package com.Automatic_Task_Reminder.task_appl.Service;

import com.Automatic_Task_Reminder.task_appl.DTO.MailDto;
import com.Automatic_Task_Reminder.task_appl.Entity.User;
import com.Automatic_Task_Reminder.task_appl.Repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailServiceClass emailServiceClass;

    public User register(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (existingUser != null) throw new RuntimeException("User already exists!");

        user.setCreatedAt(LocalDateTime.now());
        user.setVerified(false);

        String otp = String.valueOf(new SecureRandom().nextInt(900000) + 100000);
        user.setOtp(otp);
        user.setOtpExpiryTime(LocalDateTime.now().plusMinutes(5));

        MailDto mail = new MailDto.Builder()
                .sendTo(user.getEmail())
                .text("Your OTP is: " + otp)
                .subject("Verify your account")
                .build();
        emailServiceClass.sendSimpleMail(mail);

        return userRepository.save(user);
    }


    public User login(String email, String password, HttpSession session) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isVerified()) throw new RuntimeException("User is not verified. Please verify OTP first.");
        if (!user.getPassword().equals(password)) throw new RuntimeException("Invalid password");


        session.setAttribute("LoggedInUser", user.getId());
        session.setAttribute("UserId", user.getId());
        session.setAttribute("email", user.getEmail());
        session.setAttribute("name", user.getName());

        return user;
    }

    public boolean validateOtp(String enteredOtp, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getOtp() == null) throw new RuntimeException("OTP already used or expired");
        if (LocalDateTime.now().isAfter(user.getOtpExpiryTime())) throw new RuntimeException("OTP expired. Please request a new one.");

        if (user.getOtp().equals(enteredOtp)) {
            user.setVerified(true);
            user.setOtp(null);
            user.setOtpExpiryTime(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public void resendOtp(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isVerified()) throw new RuntimeException("User already verified");

        String otp = String.valueOf(new SecureRandom().nextInt(900000) + 100000);
        user.setOtp(otp);
        user.setOtpExpiryTime(LocalDateTime.now().plusMinutes(5));

        MailDto mail = new MailDto.Builder()
                .sendTo(user.getEmail())
                .text("Your new OTP is: " + otp)
                .subject("Resend OTP - Task Reminder App")
                .build();

        emailServiceClass.sendSimpleMail(mail);
        userRepository.save(user);
    }
}
