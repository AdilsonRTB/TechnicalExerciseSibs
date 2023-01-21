package cv.pn.sibs.services.interfaces;

import cv.pn.sibs.dtos.EmailDto;

public interface IEmail {

    String sendSimpleMail(EmailDto dto);

}
