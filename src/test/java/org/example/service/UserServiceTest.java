package org.example.service;

import org.example.dao.UserDao;
import org.example.model.User;
import org.example.model.UserStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Valentyn.Moliakov
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService = new UserService();

    @Before
    public void setUp() {
        User user = createDummyUser();

        when(userDao.findById(101L)).thenReturn(null);

        when(userDao.findById(1L)).thenReturn(user);
    }

    @Test(expected = RuntimeException.class)
    public void whenUserNotFoundThenThrowException() {
        userService.findById(101L);
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

    @Test(expected = RuntimeException.class)
    public void whenUserUpdatedByAnotherUserIdThenFail() {
        User user = createDummyUser2();

        userService.update(1L, user);
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