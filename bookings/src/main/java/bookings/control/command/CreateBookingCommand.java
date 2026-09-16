package bookings.control.command;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record CreateBookingCommand(
        UUID courtId,
        Instant startTime,
        List<UUID> participantIds
) {}
