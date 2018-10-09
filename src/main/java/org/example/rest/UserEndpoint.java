package org.example.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.config.rest.PATCH;
import org.example.model.User;
import org.example.model.UsersReport;
import org.example.service.UserService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 *
 */
@Stateless
@Path("/users")
@Produces("application/json")
@Consumes("application/json")
@Api(value = "Operations pertaining to users")
public class UserEndpoint {
    @Inject
    private UserService userService;

    @POST
    @ApiOperation(value = "Create new user")
    public Response create(User user) {
        User createdUser = userService.create(user);
        return Response.status(Response.Status.CREATED).entity(createdUser).build();
    }

    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    @ApiOperation(value = "Delete user")
    public Response deleteById(@PathParam("id") Long id) {
        userService.deleteById(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @ApiOperation(value = "Find user")
    public Response findById(@PathParam("id") Long id) {
        User user = userService.findById(id);
        return Response.ok(user).build();
    }

    @GET
    @ApiOperation(value = "list all users")
    public Response listAll(@QueryParam("start") Integer startPosition,
                            @QueryParam("max") Integer maxResult) {
        List<User> users = userService.listAll(startPosition, maxResult);
        GenericEntity<List<User>> entities = new GenericEntity<List<User>>(users) {
        };
        return Response.ok(entities).build();
    }

    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @ApiOperation(value = "Update user")
    public Response update(@PathParam("id") Long id, User user) {
        User updatedUser = userService.update(id, user);
        return Response.ok(updatedUser).build();
    }

    @PATCH
    @Path("/{id:[0-9][0-9]*}")
    @ApiOperation(value = "Change user status")
    public Response changeStatus(@PathParam("id") Long id, User user) {
        User updatedUser = userService.changeStatus(id, user.getStatus());
        return Response.ok(updatedUser).build();
    }

    @GET
    @Path("/report")
    @ApiOperation(value = "Generate total users report")
    public Response getUsersReport() {
        UsersReport usersReport = userService.getUsersReport();
        return Response.ok(usersReport).build();
    }
}
