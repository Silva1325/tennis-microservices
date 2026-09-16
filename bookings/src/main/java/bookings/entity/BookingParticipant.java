package bookings.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor
public class BookingParticipant {

    @Column(name = "participant_id", nullable = false)
    private UUID participantId;

    @Column(name = "participant_name", nullable = false)
    private String participantName;

    public BookingParticipant(UUID participantId, String participantName) {
        this.participantId = participantId;
        this.participantName = participantName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookingParticipant other)) return false;
        return participantId.equals(other.participantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(participantId);
    }
}
