package bookings.boundary.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateBookingRequest(
        @NotNull UUID courtId,
        @NotNull @Future Instant startTime,
        @NotNull @Size(min = 1, max = 4) List<@NotNull UUID> participantIds
) {}
