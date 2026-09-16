package bookings.control;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import io.quarkus.narayana.jta.QuarkusTransaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.hibernate.exception.ConstraintViolationException;

import bookings.client.CourtSummary;
import bookings.control.command.CreateBookingCommand;
import bookings.control.exception.CourtSlotTakenException;
import bookings.control.exception.DuplicateParticipantException;
import bookings.control.exception.InvalidStartTimeException;
import bookings.entity.BookingEntity;
import bookings.entity.BookingParticipant;

@ApplicationScoped
public class BookingCommandService {

    @Inject
    BookingRepository repository;

    @Inject
    CourtLookup courtLookup;

    @Inject
    PlayerLookup playerLookup;

    public BookingEntity create(CreateBookingCommand command) {
        requireOnTheHour(command.startTime());
        requireDistinct(command.participantIds());

        CourtSummary court = courtLookup.requireExists(command.courtId());
        Set<BookingParticipant> participants = command.participantIds().stream()
            .map(playerLookup::requireExists)
            .map(player -> new BookingParticipant(
                player.id(),
                player.firstname() + " " + player.lastname()
            )).collect(Collectors.toSet());

        return QuarkusTransaction.requiringNew().call(() -> {
            if (repository.findByCourtAndStartTime(command.courtId(), command.startTime()).isPresent()) {
                throw new CourtSlotTakenException(command.courtId(), command.startTime());
            }
            BookingEntity booking = new BookingEntity(court.id(), command.startTime(), court.name(), participants);
            repository.persist(booking);
            flushUnique(command.courtId(), command.startTime());
            return booking;
        });
    }

    private void requireOnTheHour(Instant startTime) {
        if (!startTime.truncatedTo(ChronoUnit.HOURS).equals(startTime)) {
            throw new InvalidStartTimeException(startTime);
        }
    }

    private void requireDistinct(List<UUID> participantIds) {
        if (new HashSet<>(participantIds).size() != participantIds.size()) {
            throw new DuplicateParticipantException();
        }
    }

    private void flushUnique(UUID courtId, Instant startTime) {
        try {
            repository.flush();
        } catch (ConstraintViolationException e) {
            if (BookingEntity.COURT_SLOT_CONSTRAINT.equalsIgnoreCase(e.getConstraintName())) {
                throw new CourtSlotTakenException(courtId, startTime);
            }
            throw e;
        }
    }
}
