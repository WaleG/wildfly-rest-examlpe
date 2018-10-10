package org.example.service;

import org.example.dao.ProjectDao;
import org.example.model.Project;
import org.example.model.ProjectsReport;
import org.example.model.User;
import org.example.model.UserStatus;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Valentyn.Moliakov
 */
@Stateless
public class ProjectService {

    @Inject
    private ProjectDao projectDao;

    @Inject
    private UserService userService;

    public Project create(String projectName) {
        Project project = new Project(projectName, LocalDateTime.now());
        return projectDao.create(project);
    }

    public Project findById(Long id) {
        Project project = projectDao.findById(id);
        if (project == null) {
            throw new WebApplicationException("Project with id = " + id + " not found!", Response.Status.NOT_FOUND);
        }
        return project;
    }

    public void deleteById(Long id) {
        Project project = this.findById(id);
        projectDao.delete(project);
    }

    public List<Project> listAll(Integer startPosition, Integer maxResult) {
        return projectDao.listAll(startPosition, maxResult);
    }

    public Project update(Long id, Project project) {
        if (project == null) {
            throw new WebApplicationException("Update info is not provided!", Response.Status.BAD_REQUEST);
        }
        if (id == null) {
            throw new WebApplicationException("Project id is not provided!", Response.Status.BAD_REQUEST);
        }
        if (!id.equals(project.getProjectId())) {
            throw new WebApplicationException("Provided id is not the same as updated project id!", Response.Status.CONFLICT);
        }
        this.findById(id);
        try {
            return projectDao.update(project);
        } catch (OptimisticLockException e) {
            throw new WebApplicationException("Cannot update project with id = " + id + "!", Response.Status.CONFLICT);
        }
    }

    public Project finish(Long id) {
        Project project = this.findById(id);
        project.setEndDate(LocalDateTime.now());
        return projectDao.update(project);
    }

    public Project assignUser(Long projectId, Long userId) {
        Project project = this.findById(projectId);
        if (project.isFinished()) {
            throw new WebApplicationException("Cannot assign user to finished project!", Response.Status.BAD_REQUEST);
        }
        User user = userService.findById(userId);
        if (!UserStatus.ACTIVE.equals(user.getStatus())) {
            throw new WebApplicationException("Only active users can be assigned!", Response.Status.BAD_REQUEST);
        }
        Project usersProject = user.getProject();
        if (usersProject != null && !usersProject.isFinished()) {
            throw new WebApplicationException("User is already assigned to active project", Response.Status.BAD_REQUEST);
        }
        project.addUser(user);
        try {
            return projectDao.update(project);
        } catch (OptimisticLockException e) {
            throw new WebApplicationException("Cannot update project with id = " + projectId + "!", Response.Status.CONFLICT);
        }
    }

    public Project removeUser(Long projectId, Long userId) {
        Project project = this.findById(projectId);
        User user = userService.findById(userId);
        if (!project.getUsers().contains(user)) {
            throw new WebApplicationException("Cannot remove unassigned user from project!", Response.Status.BAD_REQUEST);
        }
        project.removeUser(user);
        try {
            return projectDao.update(project);
        } catch (OptimisticLockException e) {
            throw new WebApplicationException("Cannot update project with id = " + projectId + "!", Response.Status.CONFLICT);
        }
    }

    public ProjectsReport getProjectsReport() {
        return projectDao.getProjectsReport();
    }
}
