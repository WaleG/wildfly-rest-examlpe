package org.example.config.exception;

import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author Valentyn.Moliakov
 */
@Provider
@Slf4j
public class ApplicationExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    @Produces("application/json")
    public Response toResponse(Exception exception) {
        log.error(exception.getMessage(), exception);
        ErrorResponse response = new ErrorResponse(exception.getMessage());
        if (exception instanceof WebApplicationException) {
            return Response.status(((WebApplicationException) exception).getResponse().getStatus()).entity(response).build();
        }
        return Response.serverError().entity(response).build();
    }
}
