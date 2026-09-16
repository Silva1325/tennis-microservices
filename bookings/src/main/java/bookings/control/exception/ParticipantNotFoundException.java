package bookings.control.exception;

import jakarta.ws.rs.core.Response;

import java.util.UUID;

public class ParticipantNotFoundException extends BusinessException {
    public ParticipantNotFoundException(UUID id) {
        super(Response.Status.NOT_FOUND, "Player not found: " + id);
    }
}
