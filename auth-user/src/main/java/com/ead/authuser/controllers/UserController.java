package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "userId") UUID userId) {
        Optional<UserModel> userModel = userService.findById(userId);

        userModel.ifPresentOrElse(
                user -> ResponseEntity.status(HttpStatus.OK).body(user),
        () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found")
        );

        return ResponseEntity.status(HttpStatus.OK).body(userModel.get());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "userId") UUID userId) {
        Optional<UserModel> userModel = userService.findById(userId);

        userModel.ifPresentOrElse( user ->
                userService.delete(user),
                () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found")
        );

        return ResponseEntity.status(HttpStatus.OK).body("User deleted successful.");

    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "userId") UUID userId,
                                             @JsonView(UserDto.UserView.UserPut.class) @RequestBody UserDto dto) {
        Optional<UserModel> userModel = userService.findById(userId);

        userModel.ifPresentOrElse(
               user -> {
                   user.setFullName(dto.getFullName());
                   user.setPhoneNumber(dto.getPhoneNumber());
                   user.setCPF(user.getCPF());
                   user.setLastUpdate(LocalDateTime.now(ZoneId.of("UTC")));

                   userService.save(user);

               },() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.")
       );

        return ResponseEntity.status(HttpStatus.OK).body(userModel.get());

    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updatePassword(@PathVariable(value = "userId") UUID userId,
                                             @JsonView(UserDto.UserView.PasswordPut.class) @RequestBody UserDto dto) {
        Optional<UserModel> userModel = userService.findById(userId);

        userModel.ifPresentOrElse(
                user -> {
                    if (!user.getPassword().equals(dto.getOldPassword())) {
                        throw new RuntimeException("Error: Mismatched old password");
                    }
                    user.setPassword(dto.getPassword());
                    user.setLastUpdate(LocalDateTime.now(ZoneId.of("UTC")));

                    userService.save(user);
                },
                () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found")
        );

        return ResponseEntity.status(HttpStatus.OK).body("Password updated successfully.");

    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateImage(@PathVariable(value = "userId") UUID userId,
                                                @JsonView(UserDto.UserView.ImagePut.class) @RequestBody UserDto dto) {
        Optional<UserModel> userModel = userService.findById(userId);

        userModel.ifPresentOrElse(
                user -> {
                    user.setImageUrl(dto.getImageUrl());

                    userService.save(user);
                },
                () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.")
        );

        return ResponseEntity.status(HttpStatus.OK).body("Image updated successfully.");

    }


}
