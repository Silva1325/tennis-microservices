package bookings.control.exception;

import jakarta.ws.rs.core.Response;

public class DuplicateParticipantException extends BusinessException {
    public DuplicateParticipantException() {
        super(Response.Status.BAD_REQUEST, "A player can only be listed once per booking");
    }
}
