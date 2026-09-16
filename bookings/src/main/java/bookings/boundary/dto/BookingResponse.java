package bookings.boundary.dto;

import bookings.entity.BookingEntity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record BookingResponse(
        UUID id,
        UUID courtId,
        String courtName,
        Instant startTime,
        List<ParticipantResponse> participants,
        LocalDateTime createDate,
        LocalDateTime updateDate
) {
    public static BookingResponse from(BookingEntity entity) {
        return new BookingResponse(
                entity.getPublicId(),
                entity.getCourtId(),
                entity.getCourtName(),
                entity.getStartTime(),
                entity.getParticipants().stream().map(ParticipantResponse::from).toList(),
                entity.getCreateDate(),
                entity.getUpdateDate()
        );
    }
}
