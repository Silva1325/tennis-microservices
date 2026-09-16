package bookings.control;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import bookings.entity.BookingEntity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BookingQueryService {

    @Inject
    BookingRepository repository;

    public List<BookingEntity> list() {
        return repository.listAll();
    }

    public Optional<BookingEntity> findByPublicId(UUID publicId) {
        return repository.findByPublicId(publicId);
    }
}
