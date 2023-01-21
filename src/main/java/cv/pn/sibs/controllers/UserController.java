package cv.pn.sibs.controllers;

import cv.pn.sibs.dtos.UserDto;
import cv.pn.sibs.services.interfaces.IUser;
import cv.pn.sibs.utilities.APIResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/user")
@CrossOrigin
public class UserController {

    private final IUser iUser;

    public UserController(IUser iUser) {
        this.iUser = iUser;
    }

    @Operation(summary = "get users by name or email")
    @GetMapping(path = "/{emailOrName}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Object> getUser(@PathVariable("emailOrName") String emailOrName) {

        APIResponse apiResponse = iUser.getUser(emailOrName);

        return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Created users")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserDto dto) {

        APIResponse apiResponse = iUser.createUser(dto);

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "update users by email")
    @PutMapping(path = "/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> updateUser(@RequestBody @Valid UserDto dto, @PathVariable("email") String email) {

        APIResponse apiResponse = iUser.updateUser(dto, email);

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "delete users by id user")
    @DeleteMapping(path = "/{idUser}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Object> deleteUser(@PathVariable("idUser") String idUser) {

        APIResponse apiResponse = iUser.deleteUser(idUser);

        return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);
    }
}
