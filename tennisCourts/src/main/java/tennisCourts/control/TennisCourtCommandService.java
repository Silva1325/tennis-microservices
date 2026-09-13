package tennisCourts.control;

import tennisCourts.entity.TennisCourtEntity;
import tennisCourts.entity.Surface;
import tennisCourts.control.exception.DuplicateTennisCourtException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TennisCourtCommandService {
    @Inject
    TennisCourtRepository repository;

    @Transactional
    public TennisCourtEntity create(String name, String country, String city, Surface surface) {
        if (repository.findByNameAndCity(name, city).isPresent()) {
            throw new DuplicateTennisCourtException(name, city);
        }
        TennisCourtEntity court = new TennisCourtEntity(name, country, city, surface);
        repository.persist(court);
        return court;
    }
}
