package org.example.service;

import org.example.MockitoExtension;
import org.example.dao.UserDao;
import org.example.model.User;
import org.example.model.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Valentyn.Moliakov
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService = new UserService();

    @BeforeEach
    public void setUp() {
        User user = createDummyUser();

        when(userDao.findById(101L)).thenReturn(null);

        when(userDao.findById(1L)).thenReturn(user);
    }

    @Test
    public void whenUserNotFoundThenThrowException() throws WebApplicationException{
        WebApplicationException exception = assertThrows(WebApplicationException.class,
                () -> {
                    userService.findById(101L);
                }
        );
        assertEquals("User with id = 101 not found!", exception.getMessage());
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), exception.getResponse().getStatus());
    }

    @Test
    public void whenUserFoundThenReturnUser() {
        assertEquals(userService.findById(1L), createDummyUser());
    }

    @Test
    public void whenUserUpdatedThenPass() {
        User user = createDummyUser();
        user.setSurname("Updated");

        userService.update(1L, user);
        //Using mockito ArgumentCaptor to get updated object inside of userService
        ArgumentCaptor<User> userUpdatedCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDao).update((userUpdatedCaptor.capture()));

        assertTrue(userUpdatedCaptor.getValue().equals(user));
    }

    @Test
    public void whenUserUpdatedByAnotherUserIdThenFail() throws WebApplicationException{
        WebApplicationException exception = assertThrows(WebApplicationException.class,
                () -> {
                    User user = createDummyUser2();
                    userService.update(1L, user);
                }
        );
        assertEquals("Provided id is not the same as updated user id!", exception.getMessage());
        assertEquals(Response.Status.CONFLICT.getStatusCode(), exception.getResponse().getStatus());
    }

    private User createDummyUser() {
        User user = new User();
        user.setUserId(1L);
        user.setFirstname("Sample");
        user.setSurname("User");
        user.setDateOfBirth(LocalDate.of(1975, 11, 10));
        user.setStatus(UserStatus.ACTIVE);
        return user;
    }

    private User createDummyUser2() {
        User user = new User();
        user.setUserId(2L);
        user.setFirstname("Sample 2");
        user.setSurname("User 2");
        user.setDateOfBirth(LocalDate.of(1975, 11, 10));
        user.setStatus(UserStatus.INACTIVE);
        return user;
    }
}