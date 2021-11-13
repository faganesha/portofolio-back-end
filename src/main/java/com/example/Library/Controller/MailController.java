package com.example.Library.Controller;

import com.example.Library.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController

public class MailController {
    @Autowired
    private EmailService service;

    @RequestMapping(value = "/sendemail")
    public ResponseEntity sendEmail() throws MessagingException, GeneralSecurityException {

        service.scheduleFixedRateTask();

        try {
            service.scheduleFixedRateTask();
            return ResponseEntity.status(HttpStatus.OK).body("email dikirim");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("email tidak dikirim");
        }

    }

}
