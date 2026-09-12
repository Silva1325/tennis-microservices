package tennisCourts.control;

import tennisCourts.entity.TennisCourtEntity;
import tennisCourts.entity.Surface;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TennisCourtCommandService {
    @Inject
    TennisCourtRepository repository;

    @Transactional
    public TennisCourtEntity create(String name, String country, String city, Surface surface) {
        TennisCourtEntity p = new TennisCourtEntity(name, country, city, surface);
        repository.persist(p);
        return p;
    }
}
