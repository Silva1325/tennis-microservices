package courts.control.exception;

import java.util.UUID;

import jakarta.ws.rs.core.Response;

public class CourtNotFoundException extends BusinessException {
    public CourtNotFoundException(UUID id) {
        super(Response.Status.NOT_FOUND, "Tennis court not found: " + id);
    }
}
