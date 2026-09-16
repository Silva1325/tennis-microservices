package bookings.boundary.dto;

import bookings.entity.BookingParticipant;

import java.util.UUID;

public record ParticipantResponse(UUID id, String name) {

    public static ParticipantResponse from(BookingParticipant participant) {
        return new ParticipantResponse(participant.getParticipantId(), participant.getParticipantName());
    }
}
