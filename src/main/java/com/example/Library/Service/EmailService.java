package com.example.Library.Service;

import com.example.Library.Model.Borrowment;
import com.example.Library.Repository.BorrowmentRepository;
import com.example.Library.Repository.MemberRepository;
import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service

public class EmailService {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BorrowmentRepository borrowmentRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    BorrowmentService borrowmentService;

    @Value("${spring.mail.username}")
    private String userName;

    @Value("${spring.mail.password}")
    private String password;


    public List<Borrowment> getEmailMember() {
        return memberService.getEmail();
    }

    public List<Borrowment> checkBorrowMemberLate() {
        return borrowmentRepository.findByReturnFlag(2);
    }

    public List<Borrowment> checkBorrowFlagLate() {
        return borrowmentRepository.findByEmailFlag(0);
    }

    public List<Borrowment> updateEmail() {
        return borrowmentRepository.findByEmailFlagAndReturnFlag(0, 2);
    }

    public List<String> sendEmail() throws MessagingException, GeneralSecurityException {
        List<String> member = new ArrayList<>();
        List<Borrowment> getAllMemberLate = checkBorrowFlagLate();
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);

        Properties properties = new Properties();
        properties.put("mail.imap.ssl.trust", "*");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        properties.put("mail.imap.ssl.enable", "*");
        properties.put("mail.imap.ssl.socketFactory", sf);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");


        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        });

        for (Borrowment borrowment : getAllMemberLate) {
            Message msg = new MimeMessage(session);

            String message = "Dear " + borrowment.getMember().getName() + ",\n\n" +
                    "You borrow the " + borrowment.getBook().getTittle() + " by " + borrowment.getBook().getAuthor() + " at " + borrowment.getCreatedAt() + "." +
                    "The book return day has passed and you have to pay a fine. Your total fine until today is Rp" + borrowment.getFine() + " for the lateness.";

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(borrowment.getMember().getEmail()));
            msg.setSubject("Reminder for returning book");
            msg.setText(message);

            member.add("email sent to " + borrowment.getMember().getName());

            Transport.send(msg);
            borrowment.setEmailFlag(1);

            borrowmentRepository.save(borrowment);
        }
        return (member);
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void scheduleFixedRateTask() throws MessagingException, GeneralSecurityException {
       if (!checkBorrowFlagLate().isEmpty()){
            sendEmail();
            System.out.println("test");
        } else {
           System.out.println("fail");
       }
    }
}
