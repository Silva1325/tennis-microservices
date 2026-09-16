package bookings.control.exception;

import jakarta.ws.rs.core.Response;

import java.time.Instant;
import java.util.UUID;

public class CourtSlotTakenException extends BusinessException {
    public CourtSlotTakenException(UUID courtId, Instant startTime) {
        super(Response.Status.CONFLICT, "Court " + courtId + " is already booked at " + startTime);
    }
}
