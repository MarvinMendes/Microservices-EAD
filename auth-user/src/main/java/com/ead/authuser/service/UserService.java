package com.ead.authuser.service;

import com.ead.authuser.models.UserModel;
import com.ead.authuser.specification.SpecificationTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface UserService {
    List<UserModel> getAllUsers();

    Optional<UserModel> findById(UUID userId);

    void delete(UserModel userModel);

    void save(UserModel userModel);

    boolean existByUserName(String userName);

    boolean existsByEmail(String email);

    Page<UserModel> findAll(Pageable pageable, SpecificationTemplate.UserSpec spec);
}
