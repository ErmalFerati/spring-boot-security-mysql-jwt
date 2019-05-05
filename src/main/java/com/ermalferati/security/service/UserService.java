package com.ermalferati.security.service;

import com.ermalferati.security.exception.ApplicationException;
import com.ermalferati.security.exception.BadRequestException;
import com.ermalferati.security.model.Role;
import com.ermalferati.security.model.RoleName;
import com.ermalferati.security.model.User;
import com.ermalferati.security.payload.CreateUserRequest;
import com.ermalferati.security.payload.CreateUserWithRolesRequest;
import com.ermalferati.security.payload.UserTransport;
import com.ermalferati.security.repo.RoleRepository;
import com.ermalferati.security.repo.UserRepository;
import com.ermalferati.security.util.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserTransport createWithRoles(CreateUserWithRolesRequest createUserRequest) {
        validateUsernameAndEmailAvailability(createUserRequest);

        User user = buildUserFromCreationRequest(createUserRequest);
        user = userRepository.save(user);

        return UserMapper.toTransport(user);
    }

    public UserTransport create(CreateUserRequest createUserRequest) {
        validateUsernameAndEmailAvailability(createUserRequest);

        User user = buildUserFromCreationRequest(createUserRequest);
        addDefaultRolesToUser(user);
        user = userRepository.save(user);

        return UserMapper.toTransport(user);
    }

    private void validateUsernameAndEmailAvailability(CreateUserRequest createUserRequest) {
        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            throw new BadRequestException("Email Address already in use!");
        }

        if (userRepository.existsByUsername(createUserRequest.getUsername())) {
            throw new BadRequestException("Username is already taken!");
        }
    }

    private User buildUserFromCreationRequest(CreateUserRequest createUserRequest) {
        User user = UserMapper.toEntity(createUserRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return user;
    }

    private User buildUserFromCreationRequest(CreateUserWithRolesRequest createUserWithRolesRequest) {
        User user = UserMapper.toEntity(createUserWithRolesRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return user;
    }

    private void addDefaultRolesToUser(User user) {
        Set<Role> defaultRoles = getDefaultRoles();
        user.setRoles(defaultRoles);
    }

    private Set<Role> getDefaultRoles() {
        return Collections.singleton(roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new ApplicationException("User Role not set.")));
    }
}
