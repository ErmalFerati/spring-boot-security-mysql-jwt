package com.ermalferati.security;

import com.ermalferati.security.model.Role;
import com.ermalferati.security.model.RoleName;
import com.ermalferati.security.model.User;
import com.ermalferati.security.payload.CreateUserRequest;
import com.ermalferati.security.payload.CreateUserWithRolesRequest;
import com.ermalferati.security.payload.UserTransport;

import java.util.Collections;
import java.util.Set;

public final class UserServiceTestUtil {

    private UserServiceTestUtil() {

    }

    public static CreateUserRequest buildValidCreateUserRequest() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setName("Ermal Ferati");
        createUserRequest.setUsername("Ermal");
        createUserRequest.setPassword("Ferati");
        createUserRequest.setEmail("ermalferati@gmail.com");

        return createUserRequest;
    }

    public static CreateUserWithRolesRequest buildValidCreateUserWithRolesRequest() {
        CreateUserWithRolesRequest createUserWithRolesRequest = new CreateUserWithRolesRequest();
        createUserWithRolesRequest.setName("Ermal Ferati");
        createUserWithRolesRequest.setUsername("Ermal");
        createUserWithRolesRequest.setPassword("Ferati");
        createUserWithRolesRequest.setEmail("ermalferati@gmail.com");

        Set<Role> userRoles = buildDefaultUserRoles();
        createUserWithRolesRequest.setRoles(userRoles);

        return createUserWithRolesRequest;
    }

    public static User buildUser() {
        User user = new User();
        user.setName("Ermal Ferati");
        user.setUsername("Ermal");
        user.setEmail("ermalferati@gmail.com");

        Set<Role> defaultUserRoles = buildDefaultUserRoles();
        user.setRoles(defaultUserRoles);

        return user;
    }

    public static Set<Role> buildDefaultUserRoles() {
        Role userRole = buildUserRole();
        return Collections.singleton(userRole);
    }

    public static Role buildUserRole() {
        Role role = new Role();
        role.setName(RoleName.ROLE_USER);
        return role;
    }

    public static Set<String> buildDefaultUserRoleNames() {
        Role userRole = buildUserRole();
        String userRoleName = userRole.getName().name();
        return Collections.singleton(userRoleName);
    }

    public static boolean validateCreatedUser(CreateUserRequest createUserRequest, UserTransport createdUserTransport) {
        Set<String> defaultUserRoleNames = buildDefaultUserRoleNames();
        return createUserRequest.getUsername().equals(createdUserTransport.getUsername())
                && createUserRequest.getEmail().equals(createdUserTransport.getEmail())
                && createUserRequest.getName().equals(createdUserTransport.getName())
                && createdUserTransport.getRoles().equals(defaultUserRoleNames);
    }
}
