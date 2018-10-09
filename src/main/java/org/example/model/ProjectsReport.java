package org.example.model;

import lombok.Data;

/**
 * @author Valentyn.Moliakov
 */
@Data
public class ProjectsReport {
    private long activeProjects;
    private long finishedProjects;

    public ProjectsReport() {

    }

    public ProjectsReport(long activeProjects, long finishedProjects) {
        this.activeProjects = activeProjects;
        this.finishedProjects = finishedProjects;
    }
}
