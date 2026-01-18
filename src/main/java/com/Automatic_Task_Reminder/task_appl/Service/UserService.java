package com.Automatic_Task_Reminder.task_appl.Service;

import com.Automatic_Task_Reminder.task_appl.DTO.MailDto;
import com.Automatic_Task_Reminder.task_appl.DTO.UserProfileDto;
import com.Automatic_Task_Reminder.task_appl.Entity.User;
import com.Automatic_Task_Reminder.task_appl.Repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    private UserRepository userRepository;

    private EmailServiceClass emailServiceClass;

    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder,
                       UserRepository userRepository,
                       EmailServiceClass emailServiceClass) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.emailServiceClass = emailServiceClass;
    }


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
user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    public User login(String email, String password, HttpSession session) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isVerified()) throw new RuntimeException("User is not verified. Please verify OTP first.");
        if (!bCryptPasswordEncoder.matches(password,user.getPassword())) throw new RuntimeException("Invalid password");


        session.setAttribute("loggedUser", user);


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
    public void uploadProfileImage(Integer id, MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        if (!file.getContentType().startsWith("image/")) {
            throw new RuntimeException("Only image files allowed");
        }

        if (file.getSize() > 1 * 1024 * 1024) {
            throw new RuntimeException("Image must be less than 1MB");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));


        user.setProfileImage(file.getBytes());

        userRepository.save(user);
    }
    public UserProfileDto mapToProfileDTO(User user) {
        String imageBase64 = null;

        if (user.getProfileImage() != null && user.getProfileImage().length > 0) {
            imageBase64 = Base64.getEncoder()
                    .encodeToString(user.getProfileImage());
        }

        return new UserProfileDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.isVerified(),
                user.getCreatedAt(),
                imageBase64
        );
    }
    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void saveUser(User loggedUser) {
        userRepository.save(loggedUser);
    }

    public void removeProfileImage(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setProfileImage(null);          // byte[]

        userRepository.save(user);
    }

}
