package bookings.control.exception;

import jakarta.ws.rs.core.Response;

import java.util.UUID;

public class BookingNotFoundException extends BusinessException {
    public BookingNotFoundException(UUID id) {
        super(Response.Status.NOT_FOUND, "Booking not found: " + id);
    }
}
