package mos.quarkus.play.controller;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BadRequestExceptionHandler implements ExceptionMapper<BadRequestException> {
    @Override
    public Response toResponse(BadRequestException exception) {
        String detailedMessage = exception.getMessage();
        if (exception.getCause() != null) {
            detailedMessage += "; Cause: " + exception.getCause().getMessage();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("Wrong input data: " + detailedMessage)
                .build();
    }
}
