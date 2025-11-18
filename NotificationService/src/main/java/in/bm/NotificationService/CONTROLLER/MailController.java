package in.bm.NotificationService.CONTROLLER;

import in.bm.NotificationService.CONFIG.WebClientImp;
import in.bm.NotificationService.DTO.MailRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private WebClientImp webClientImp;

    @GetMapping("/register")
    public void registrationMail(@RequestParam String to, @RequestParam String username) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("bharatmarwah4@gmail.com");
        mailMessage.setTo(to);
        mailMessage.setSubject("Welcome to Kitflik – Registration Successful 🎉");
        mailMessage.setText("Hello " + username + ",\n\n" +
                "Thank you for registering with Kitflik 🎉.\n\n" +
                "Happy Exploring!\nThe Kitflik Team");

        mailSender.send(mailMessage);
        log.info("Registration mail sent to {}", to);
    }

    @GetMapping("/otp")
    public void otpMail(@RequestParam String otp,
                        @RequestParam String username,
                        @RequestParam String to) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("bharatmarwah4@gmail.com");
        mailMessage.setTo(to);
        mailMessage.setSubject("Your Kitflik Password Reset OTP");
        mailMessage.setText("Hello " + username + ",\n\n" +
                "Your OTP is: " + otp + "\n" +
                "It expires in 5 minutes.\n\n" +
                "If you did not request this, ignore this email.\n\n" +
                "The Kitflik Team");

        mailSender.send(mailMessage);
        log.info("OTP mail sent to {}", to);
    }


    // Every Friday @ 12:00 PM
    @Scheduled(cron = "0 0 12 ? * FRI")
    public void scheduledMail() {

        log.info("Scheduled mail job started…");

        List<String> emails = webClientImp.getEmails();

        if (emails == null || emails.isEmpty()) {
            log.warn("No emails found for scheduled mailing.");
            return;
        }

        for (String e : emails) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("bharatmarwah4@gmail.com");
                message.setTo(e);
                message.setSubject("🍿 New Movies Released on KitFlik 🎬");
                message.setText("Hi there!\n\n" +
                        "New movies have dropped on KitFlik! 🎉\n" +
                        "Log in and enjoy the latest releases.\n\n" +
                        "The KitFlik Team");

                mailSender.send(message);
                log.info("Scheduled mail sent to {}", e);

            } catch (Exception ex) {
                log.error("Failed to send scheduled mail to {}: {}", e, ex.getMessage());
            }
        }

        log.info("Scheduled mail job finished.");
    }

    @PostMapping("/confirmation")
    public void sendConfirmationMail(@RequestBody MailRequest request) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("bharatmarwah4@gmail.com");
        mailMessage.setTo(request.getToEmail());
        mailMessage.setSubject(request.getSubject());
        mailMessage.setText(request.getBody());

        mailSender.send(mailMessage);
        log.info("Confirmation mail sent to {}", request.getToEmail());
    }
}
