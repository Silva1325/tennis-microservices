package tennisCourts.control;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import tennisCourts.entity.TennisCourtEntity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TennisCourtQueryService {
    @Inject
    TennisCourtRepository repository;

    public List<TennisCourtEntity> list() {
        return repository.listAll();
    }

    public Optional<TennisCourtEntity> findByPublicId(UUID publicId) {
        return repository.findByPublicId(publicId);
    }
}
