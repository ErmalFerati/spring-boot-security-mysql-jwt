package com.ermalferati.security.service;

import com.ermalferati.security.exception.ApplicationException;
import com.ermalferati.security.exception.BadRequestException;
import com.ermalferati.security.model.Role;
import com.ermalferati.security.model.User;
import com.ermalferati.security.payload.CreateUserRequest;
import com.ermalferati.security.payload.CreateUserWithRolesRequest;
import com.ermalferati.security.payload.UserTransport;
import com.ermalferati.security.repo.RoleRepository;
import com.ermalferati.security.repo.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static com.ermalferati.security.UserServiceTestUtil.buildUser;
import static com.ermalferati.security.UserServiceTestUtil.buildUserRole;
import static com.ermalferati.security.UserServiceTestUtil.buildValidCreateUserRequest;
import static com.ermalferati.security.UserServiceTestUtil.buildValidCreateUserWithRolesRequest;
import static com.ermalferati.security.UserServiceTestUtil.validateCreatedUser;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;
    private Role defaultUserRole;
    private CreateUserRequest createUserRequest;
    private CreateUserWithRolesRequest createUserWithRolesRequest;

    @Before
    public void setup() {
        userService = new UserService(userRepository, roleRepository, passwordEncoder);

        user = buildUser();
        defaultUserRole = buildUserRole();
        createUserRequest = buildValidCreateUserRequest();
        createUserWithRolesRequest = buildValidCreateUserWithRolesRequest();
    }

    @Test
    public void createUser_ValidUser_CreatesNewUser() {
        mockUserSavingInRepository();
        mockDefaultUserRoleInRepository();

        UserTransport createdUserTransport = userService.create(createUserRequest);
        assertTrue(validateCreatedUser(createUserRequest, createdUserTransport));
    }

    @Test(expected = ApplicationException.class)
    public void createUser_ValidUserMissingRolesInDatabase_ThrowsApplicationException() {
        userService.create(createUserRequest);
    }

    @Test(expected = BadRequestException.class)
    public void createUser_UserWithExistingEmail_ThrowsApplicationException() {
        mockUserEmailUsed();

        userService.create(createUserRequest);
    }

    @Test(expected = BadRequestException.class)
    public void createUser_UserWithExistingUsername_ThrowsApplicationException() {
        mockUserUsernameUsed();

        userService.create(createUserRequest);
    }

    @Test
    public void createUserWithRoles_ValidUser_CreatesNewUser() {
        mockUserSavingInRepository();
        mockDefaultUserRoleInRepository();

        UserTransport createdUserTransport = userService.createWithRoles(createUserWithRolesRequest);
        assertTrue(validateCreatedUser(createUserWithRolesRequest, createdUserTransport));
    }

    @Test(expected = BadRequestException.class)
    public void createUserWithRoles_UserWithExistingEmail_ThrowsApplicationException() {
        mockUserEmailUsed();

        userService.createWithRoles(createUserWithRolesRequest);
    }

    @Test(expected = BadRequestException.class)
    public void createUserWithRoles_UserWithExistingUsername_ThrowsApplicationException() {
        mockUserUsernameUsed();

        userService.create(createUserWithRolesRequest);
    }

    private void mockUserEmailUsed() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
    }

    private void mockUserUsernameUsed() {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
    }

    private void mockUserSavingInRepository() {
        when(userRepository.save(any())).thenReturn(user);
    }

    private void mockDefaultUserRoleInRepository() {
        when(roleRepository.findByName(any())).thenReturn(Optional.of(defaultUserRole));
    }
}
