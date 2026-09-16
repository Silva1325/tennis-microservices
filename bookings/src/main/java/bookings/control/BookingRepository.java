package bookings.control;

import bookings.entity.BookingEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class BookingRepository implements PanacheRepository<BookingEntity> {

    public Optional<BookingEntity> findByPublicId(UUID publicId) {
        return find("publicId", publicId).firstResultOptional();
    }

    public Optional<BookingEntity> findByCourtAndStartTime(UUID courtId, Instant startTime) {
        return find("courtId = ?1 and startTime = ?2", courtId, startTime).firstResultOptional();
    }

}
