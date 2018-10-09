package org.example.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.config.rest.PATCH;
import org.example.model.Project;
import org.example.model.ProjectsReport;
import org.example.model.User;
import org.example.service.ProjectService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 *
 */
@Stateless
@Path("/projects")
@Produces("application/json")
@Consumes("application/json")
@Api(value = "Operations pertaining to projects")
public class ProjectEndpoint {
    @Inject
    private ProjectService projectService;

    @POST
    @ApiOperation(value = "Create new project")
    public Response create(@Valid Project project) {
        Project createdProject = projectService.create(project.getName());
        return Response.status(Response.Status.CREATED).entity(createdProject).build();
    }

    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    @ApiOperation(value = "Delete project")
    public Response deleteById(@PathParam("id") Long id) {
        projectService.deleteById(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @ApiOperation(value = "Find project")
    public Response findById(@PathParam("id") Long id) {
        Project project = projectService.findById(id);
        return Response.ok(project).build();
    }

    @GET
    @ApiOperation(value = "List all projects")
    public Response listAll(@QueryParam("start") Integer startPosition,
                            @QueryParam("max") Integer maxResult) {
        List<Project> projects = projectService.listAll(startPosition, maxResult);
        GenericEntity<List<Project>> entities = new GenericEntity<List<Project>>(projects) {
        };
        return Response.ok(entities).build();
    }

    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @ApiOperation(value = "Update project")
    public Response update(@PathParam("id") Long id, Project project) {
        Project updatedProject = projectService.update(id, project);
        return Response.ok(updatedProject).build();
    }

    @PATCH
    @Path("/{id:[0-9][0-9]*}")
    @ApiOperation(value = "Finish project")
    public Response finish(@PathParam("id") Long id) {
        Project finishedProject = projectService.finish(id);
        return Response.ok(finishedProject).build();
    }

    @PUT
    @Path("/{id:[0-9][0-9]*}/users")
    @ApiOperation(value = "Assign active user to active project")
    public Response assignUser(@PathParam("id") Long id, User user) {
        Project project = projectService.assignUser(id, user.getUserId());
        return Response.ok(project).build();
    }

    @DELETE
    @Path("/{id:[0-9][0-9]*}/users")
    @ApiOperation(value = "Remove user from project")
    public Response removeUser(@PathParam("id") Long id, User user) {
        Project project = projectService.removeUser(id, user.getUserId());
        return Response.ok(project).build();
    }

    @GET
    @Path("/report")
    @ApiOperation(value = "Generate total projects report")
    public Response getProjectsReport() {
        ProjectsReport projectsReport = projectService.getProjectsReport();
        return Response.ok(projectsReport).build();
    }
}
