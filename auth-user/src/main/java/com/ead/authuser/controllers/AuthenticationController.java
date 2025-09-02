package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.models.enums.UserStatus;
import com.ead.authuser.models.enums.UserType;
import com.ead.authuser.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    @Autowired
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody @JsonView(UserDto.UserView.RegistrationPost.class)
                                                   @Validated(UserDto.UserView.RegistrationPost.class) UserDto dto) {
       if(userService.existByUserName(dto.getUserName())) {
           return ResponseEntity.status(HttpStatus.CONFLICT).body("Error. Username is already taken!");
       }

       if(userService.existsByEmail(dto.getEmail())) {
           return ResponseEntity.status(HttpStatus.CONFLICT).body("Error. Email is already taken!");
       }

       var userModel = new UserModel();

       BeanUtils.copyProperties(dto, userModel);
       userModel.setUserStatus(UserStatus.ACTIVE);
       userModel.setUserType(UserType.STUDENT);
       userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
       userModel.setLastUpdate(LocalDateTime.now(ZoneId.of("UTC")));

       userService.save(userModel);

       return ResponseEntity.status(HttpStatus.OK).body(userModel);
    }



}
