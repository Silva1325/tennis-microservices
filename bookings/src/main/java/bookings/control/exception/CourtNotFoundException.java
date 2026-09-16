package bookings.control.exception;

import jakarta.ws.rs.core.Response;

import java.util.UUID;

public class CourtNotFoundException extends BusinessException {
    public CourtNotFoundException(UUID id) {
        super(Response.Status.NOT_FOUND, "Court not found: " + id);
    }
}
