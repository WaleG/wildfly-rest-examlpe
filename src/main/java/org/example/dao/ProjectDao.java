package org.example.dao;

import org.example.model.Project;
import org.example.model.ProjectsReport;

import java.util.List;

/**
 * @author Valentyn.Moliakov
 */
public interface ProjectDao {
    Project create(Project project);

    void deleteById(Long id);

    void delete(Project project);

    Project findById(Long id);

    Project update(Project project);

    List<Project> listAll(Integer startPosition, Integer maxResult);

    ProjectsReport getProjectsReport();
}
