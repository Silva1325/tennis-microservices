package bookings.client;

import java.util.UUID;

public record CourtSummary(UUID id, String name, String city) {}