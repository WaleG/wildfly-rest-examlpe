package org.example.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.dao.ProjectDao;
import org.example.model.Project;
import org.example.model.ProjectsReport;
import org.example.model.UserStatus;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * DAO for Project
 */
@Stateless
@Slf4j
public class ProjectDaoImpl implements ProjectDao {
    @PersistenceContext(unitName = "wildfly-rest-persistence-unit")
    private EntityManager em;

    @Override
    public Project create(Project project) {
        em.persist(project);
        em.flush();
        log.info("Project {} created Successfully.", project.getName());
        return project;
    }

    @Override
    public void deleteById(Long id) {
        Project project = em.find(Project.class, id);
        if (project != null) {
            em.remove(project);
        }
        log.info("Project deleted Successfully.");
    }

    @Override
    public void delete(Project project) {
        em.remove(project);
        log.info("Project deleted Successfully.");
    }

    @Override
    public Project findById(Long id) {
        return em.find(Project.class, id);
    }

    @Override
    public Project update(Project project) {
        em.merge(project);
        log.info("Project {} updated Successfully.", project.getName());
        return project;
    }

    @Override
    public List<Project> listAll(Integer startPosition, Integer maxResult) {
        TypedQuery<Project> findAllQuery = em
                .createQuery(
                        "SELECT DISTINCT p FROM Project p LEFT JOIN FETCH p.users ORDER BY p.projectId",
                        Project.class);
        if (startPosition != null) {
            findAllQuery.setFirstResult(startPosition);
        }
        if (maxResult != null) {
            findAllQuery.setMaxResults(maxResult);
        }
        return findAllQuery.getResultList();
    }

    @Override
    public ProjectsReport getProjectsReport() {
        long activeProjects = em.createQuery("SELECT count(p.projectId) FROM Project p where p.endDate is null", Long.class).getSingleResult();
        long finishedProjects = em.createQuery("SELECT count(p.projectId) FROM Project p where p.endDate is not null", Long.class).getSingleResult();
        return new ProjectsReport(activeProjects, finishedProjects);
    }

}
