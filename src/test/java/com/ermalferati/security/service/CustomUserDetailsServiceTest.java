package com.ermalferati.security.service;

import com.ermalferati.security.model.User;
import com.ermalferati.security.model.UserPrincipal;
import com.ermalferati.security.repo.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static com.ermalferati.security.UserServiceTestUtil.buildUser;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class CustomUserDetailsServiceTest {

    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private UserRepository userRepository;

    private User user;

    @Before
    public void setup() {
        customUserDetailsService = new CustomUserDetailsService(userRepository);
        user = buildUser();
    }

    @Test
    public void loadUserById_UserExists_ReturnsUserPrincipal() {
        mockUserById();

        UserPrincipal userPrincipal = (UserPrincipal) customUserDetailsService.loadUserById(user.getId());
        validateReturnedUserPrincipal(userPrincipal, user);
    }

    @Test(expected = EntityNotFoundException.class)
    public void loadUserById_UserDoesNotExist_ThrowsEntityNotFoundException() {
        long notExistingUserId = 123;
        customUserDetailsService.loadUserById(notExistingUserId);
    }

    @Test
    public void loadUserByUsername_UserWithUsernameExists_ReturnsUserPrincipal() {
        mockUserByUsername();

        UserPrincipal userPrincipal = (UserPrincipal) customUserDetailsService.loadUserByUsername(user.getUsername());
        validateReturnedUserPrincipal(userPrincipal, user);
    }

    @Test
    public void loadUserByUsername_UserWithEmailExists_ReturnsUserPrincipal() {
        mockUserByEmail();

        UserPrincipal userPrincipal = (UserPrincipal) customUserDetailsService.loadUserByUsername(user.getEmail());
        validateReturnedUserPrincipal(userPrincipal, user);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserById_UserWithUsernameOrEmailDoesNotExist_ThrowsUsernameNotFoundException() {
        String notExistingUserUsernameOrEmail = "I-dont-exist";
        customUserDetailsService.loadUserByUsername(notExistingUserUsernameOrEmail);
    }

    private void mockUserById() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    }

    private void mockUserByUsername() {
        when(userRepository.findByUsernameOrEmail(user.getUsername(), user.getUsername())).thenReturn(Optional.of(user));
    }

    private void mockUserByEmail() {
        when(userRepository.findByUsernameOrEmail(user.getEmail(), user.getEmail())).thenReturn(Optional.of(user));
    }

    private void validateReturnedUserPrincipal(UserPrincipal userPrincipal, User user) {
        assertEquals(userPrincipal.getId(), user.getId());
        assertEquals(userPrincipal.getName(), user.getName());
        assertEquals(userPrincipal.getUsername(), user.getUsername());
        assertEquals(userPrincipal.getPassword(), user.getPassword());
        assertEquals(userPrincipal.getEmail(), user.getEmail());
    }

}
