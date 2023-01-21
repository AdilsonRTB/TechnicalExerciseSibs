package cv.pn.sibs.services.implementations;

import cv.pn.sibs.dtos.EmailDto;
import cv.pn.sibs.services.interfaces.IEmail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailImpl implements IEmail {

    Logger logger = LogManager.getLogger(EmailImpl.class);

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}") private String sender;

    public EmailImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    public String sendSimpleMail(EmailDto details) {

        try {

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());

            javaMailSender.send(mailMessage);

            logger.info("Mail Sent " + details.getRecipient() + "Successfully");
            return "Mail Sent Successfully...";
        }

        catch (Exception e) {
            logger.error("Error while Sending Mail");
            return "Error while Sending Mail";
        }
    }

}
