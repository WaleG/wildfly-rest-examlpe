package org.example.service;

import org.example.dao.ProjectDao;
import org.example.model.Project;
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
import java.time.LocalDateTime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Valentyn.Moliakov
 */
@RunWith(MockitoJUnitRunner.class)
public class ProjectServiceTest {

    @Mock
    private ProjectDao projectDao;

    @Mock
    private UserService userService;

    @InjectMocks
    private ProjectService projectService = new ProjectService();

    @Before
    public void setUp() {
        User user = createDummyUser();

        when(projectDao.findById(101L)).thenReturn(null);

        when(projectDao.findById(1L)).thenReturn(createDummyProjectWithUser(user));
        when(userService.findById(1L)).thenReturn(user);

        when(userService.findById(2L)).thenReturn(createDummyUser2());
        when(userService.findById(3L)).thenReturn(createInactiveUser());

        when(projectDao.findById(2L)).thenReturn(createDummyProjectWithoutUser());
    }

    @Test(expected = RuntimeException.class)
    public void whenProjectNotFoundThenThrowException() {
        projectService.findById(101L);
    }

    @Test
    public void whenProjectFoundThenReturnUser() {
        assertEquals(projectService.findById(2L), createDummyProjectWithoutUser());
    }

    @Test
    public void whenAssignedUserRemovedThenPass() {
        Project project = projectService.findById(1L);
        assertFalse(project.getUsers().isEmpty());

        projectService.removeUser(project.getProjectId(), project.getUsers().get(0).getUserId());
        //Using mockito ArgumentCaptor to get updated object inside of projectService
        ArgumentCaptor<Project> projectUpdatedCaptor = ArgumentCaptor.forClass(Project.class);
        verify(projectDao).update((projectUpdatedCaptor.capture()));

        assertTrue(projectUpdatedCaptor.getValue().getUsers().isEmpty());
    }

    @Test(expected = RuntimeException.class)
    public void whenNotAssignedUserRemovedThenFail() {
        Project project = projectService.findById(2L);
        projectService.removeUser(project.getProjectId(), createDummyUser().getUserId());
    }

    @Test
    public void whenActiveUserAssignedThenPass() {
        projectService.assignUser(2L, 2L);

        //Using mockito ArgumentCaptor to get updated object inside of projectService
        ArgumentCaptor<Project> projectUpdatedCaptor = ArgumentCaptor.forClass(Project.class);
        verify(projectDao).update((projectUpdatedCaptor.capture()));

        assertFalse(projectUpdatedCaptor.getValue().getUsers().isEmpty());
    }

    @Test(expected = RuntimeException.class)
    public void whenInactiveUserAssignedThenFail() {
        projectService.assignUser(2L, 3L);
    }

    private Project createDummyProjectWithUser(User user) {
        Project project = new Project();
        project.setName("Dummy project");
        project.setStartDate(LocalDateTime.of(2018, 5, 26, 18, 10));
        project.setProjectId(1L);
        project.addUser(user);
        return project;
    }

    private Project createDummyProjectWithoutUser() {
        Project project = new Project();
        project.setName("Dummy project 2");
        project.setStartDate(LocalDateTime.of(2017, 10, 20, 12, 50));
        project.setProjectId(2L);
        return project;
    }

    private User createDummyUser() {
        User user = new User();
        user.setUserId(1L);
        user.setFirstname("Sam");
        user.setSurname("Dummy");
        user.setDateOfBirth(LocalDate.of(1993, 3, 3));
        user.setStatus(UserStatus.ACTIVE);
        return user;
    }

    private User createDummyUser2() {
        User user = new User();
        user.setUserId(2L);
        user.setFirstname("Frank");
        user.setSurname("Smith");
        user.setDateOfBirth(LocalDate.of(1980, 10, 10));
        user.setStatus(UserStatus.ACTIVE);
        return user;
    }

    private User createInactiveUser() {
        User user = new User();
        user.setUserId(3L);
        user.setFirstname("Petter");
        user.setSurname("Times");
        user.setDateOfBirth(LocalDate.of(1950, 12, 19));
        user.setStatus(UserStatus.INACTIVE);
        return user;
    }
}