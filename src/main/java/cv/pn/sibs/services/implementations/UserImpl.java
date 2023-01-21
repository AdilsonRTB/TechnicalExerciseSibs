package cv.pn.sibs.services.implementations;

import cv.pn.sibs.dtos.UserDto;
import cv.pn.sibs.entities.User;
import cv.pn.sibs.repositories.UserRepository;
import cv.pn.sibs.services.interfaces.IUser;
import cv.pn.sibs.utilities.APIResponse;
import cv.pn.sibs.utilities.APIUtilities;
import cv.pn.sibs.utilities.MessageState;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserImpl implements IUser {

    private final UserRepository userRepository;

    public UserImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public APIResponse getUser(String emailOrName) {

        try {

            List<User> users = userRepository.findByEmailLikeOrNameLike("%" + emailOrName + "%", "%" + emailOrName + "%");
            List<Object> objects = new ArrayList<>(users);


            return APIResponse.builder().status(true).statusText(MessageState.SUCCESS).details(objects).build();

        } catch (Exception e ) {

            return APIResponse.builder().status(false).statusText(MessageState.ERROR).details(Collections.singletonList(e.getMessage())).build();
        }
    }

    @Override
    public APIResponse deleteUser(String id) {

        Optional<User> optionalUser = userRepository.findById(id.trim());
        APIUtilities.checkResource(optionalUser, "User " + id.trim() + " " + MessageState.DONT_EXIST);
        User user = optionalUser.get();

        try {

            userRepository.delete(user);

            return APIResponse.builder().status(true).statusText(MessageState.DELETE_SUCCESS).build();

        } catch (Exception e ) {

            return APIResponse.builder().status(false).statusText(MessageState.DELETE_ERROR).details(Collections.singletonList(e.getMessage())).build();
        }
    }

    @Override
    public APIResponse updateUser(UserDto dto, String email) {

        Optional<User> optionalUser = userRepository.findByEmail(email);
        APIUtilities.checkResource(optionalUser, email + " " + MessageState.DONT_EXIST);

        if (!email.equals(dto.getEmail())) {
            Optional<User> optionalUserExist = userRepository.findByEmail(dto.getEmail());
            if (optionalUserExist.isPresent()) {
                return APIResponse.builder().status(false).statusText(MessageState.CREATE_ERROR).details(Collections.singletonList(dto.getEmail() + " " + MessageState.ALREADY_EXIST)).build();
            }
        }

        try {

            optionalUser.get().setName(dto.getName());
            optionalUser.get().setEmail(dto.getEmail());
            userRepository.saveAndFlush(optionalUser.get());

            return APIResponse.builder().status(true).statusText(MessageState.UPDATE_SUCCESS).details(Collections.singletonList(optionalUser.get())).build();

        } catch (Exception e ) {

            return APIResponse.builder().status(false).statusText(MessageState.UPDATE_ERROR).details(Collections.singletonList(e.getMessage())).build();
        }
    }

    @Override
    public APIResponse createUser(UserDto dto) {

        Optional<User> optionalUser = userRepository.findByEmail(dto.getEmail());
        if(optionalUser.isPresent()) {
            return APIResponse.builder().status(false).statusText(MessageState.CREATE_ERROR).details(Collections.singletonList(dto.getEmail() + " " + MessageState.ALREADY_EXIST)).build();
        }

        User user = new User();

        try {

            BeanUtils.copyProperties(dto, user);
            user.setStatus(true);
            userRepository.save(user);

            return APIResponse.builder().status(true).statusText(MessageState.CREATE_SUCCESS).details(Collections.singletonList(user)).build();

        } catch (Exception e ) {

            return APIResponse.builder().status(false).statusText(MessageState.CREATE_ERROR).details(Collections.singletonList(e.getMessage())).build();
        }
    }
}
