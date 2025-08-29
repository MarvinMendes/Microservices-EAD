package com.ead.authuser.dtos;

import com.ead.authuser.models.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private UUID userId;

    private String userName;

    private String email;

    private String password;

    private String oldPassword;

    private String fullName;

    private String phoneNumber;

    private String CPF;

    private String imageUrl;
}
