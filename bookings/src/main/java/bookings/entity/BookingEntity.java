package bookings.entity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = BookingEntity.COURT_SLOT_CONSTRAINT, columnNames = {"court_id", "start_time"}))
@Getter
@NoArgsConstructor
public class BookingEntity extends AbstractEntity {

    public static final String COURT_SLOT_CONSTRAINT = "uk_booking_court_slot";

    @Column(name = "court_id", nullable = false)
    private UUID courtId;

    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(name = "court_name", nullable = false)
    @Setter
    private String courtName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "booking_participant",
            joinColumns = @JoinColumn(name = "booking_id"),
            uniqueConstraints = @UniqueConstraint(name = "uk_booking_participant", columnNames = {"booking_id", "participant_id"})
    )
    private Set<BookingParticipant> participants = new HashSet<>();

    public BookingEntity(UUID courtId, Instant startTime, String courtName, Set<BookingParticipant> participants) {
        this.courtId = courtId;
        this.startTime = startTime;
        this.courtName = courtName;
        this.participants = new HashSet<>(participants);
    }
}
