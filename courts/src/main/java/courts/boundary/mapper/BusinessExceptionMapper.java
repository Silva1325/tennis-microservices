package courts.boundary.mapper;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import courts.boundary.dto.ErrorResponse;
import courts.control.exception.BusinessException;

@Provider
public class BusinessExceptionMapper implements ExceptionMapper<BusinessException> {
    @Override
    public Response toResponse(BusinessException e) {
        return Response.status(e.getStatus())
                .entity(new ErrorResponse(e.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
