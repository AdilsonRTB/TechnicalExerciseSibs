package cv.pn.sibs.controllers;

import cv.pn.sibs.dtos.EmailDto;
import cv.pn.sibs.services.interfaces.IEmail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    private static final Logger logger = LogManager.getLogger(EmailController.class);
    private final IEmail iEmail;

    public EmailController(IEmail iEmail) {
        this.iEmail = iEmail;
    }

    @PostMapping("/sendMail")
    public String
    sendMail(@RequestBody EmailDto details) {

        String status = iEmail.sendSimpleMail(details);

        logger.info("Info log");


        return status;
    }
}
