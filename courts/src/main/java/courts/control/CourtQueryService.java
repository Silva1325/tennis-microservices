package courts.control;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import courts.entity.CourtEntity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CourtQueryService {
    @Inject
    CourtRepository repository;

    public List<CourtEntity> list() {
        return repository.listAll();
    }

    public Optional<CourtEntity> findByPublicId(UUID publicId) {
        return repository.findByPublicId(publicId);
    }
}
