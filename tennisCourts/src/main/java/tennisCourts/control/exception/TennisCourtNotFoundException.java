package tennisCourts.control.exception;

import java.util.UUID;

import jakarta.ws.rs.core.Response;

public class TennisCourtNotFoundException extends BusinessException {
    public TennisCourtNotFoundException(UUID id) {
        super(Response.Status.NOT_FOUND, "Tennis court not found: " + id);
    }
}
