package bookings.control.exception;

import jakarta.ws.rs.core.Response;

import java.time.Instant;

public class InvalidStartTimeException extends BusinessException {
    public InvalidStartTimeException(Instant startTime) {
        super(Response.Status.BAD_REQUEST, "Bookings must start on the hour: " + startTime);
    }
}
