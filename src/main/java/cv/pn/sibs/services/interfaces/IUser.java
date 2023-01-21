package cv.pn.sibs.services.interfaces;

import cv.pn.sibs.dtos.UserDto;
import cv.pn.sibs.utilities.APIResponse;
public interface IUser {

    APIResponse getUser(String emailOrName);

    APIResponse deleteUser(String id);

    APIResponse updateUser(UserDto dto, String email);

    APIResponse createUser(UserDto dto);
}
