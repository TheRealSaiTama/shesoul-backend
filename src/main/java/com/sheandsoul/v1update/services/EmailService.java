package com.sheandsoul.v1update.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mode:prod}")
    private String appMode;

    @Value("${spring.mail.from}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtpEmail(String to, String otp) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            // Set the "From" address to match your Zoho username
            helper.setFrom(fromEmail);

            String emailContent = "<h3>Hello,</h3>"
                                + "<p>Thank you for signing up. Please use the following One-Time Password (OTP) to verify your email address:</p>"
                                + "<h2>" + otp + "</h2>"
                                + "<p>This OTP is valid for 10 minutes. If you did not request this, please ignore this email.</p>"
                                + "<br>"
                                + "<p>Best regards,<br>She&Soul Team</p>";

            helper.setText(emailContent, true); // true indicates HTML content
            helper.setTo(to);
            helper.setSubject("Your Email Verification Code");

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.err.println("Failed to send OTP email: " + e.getMessage());
            throw new IllegalStateException("Failed to send OTP email.");
        }
    }
}
