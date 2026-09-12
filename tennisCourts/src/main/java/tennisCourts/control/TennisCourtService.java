package tennisCourts.control;

import java.util.List;
import java.util.Optional;

import tennisCourts.entity.TennisCourtEntity;
import tennisCourts.entity.Surface;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TennisCourtService implements PanacheRepository<TennisCourtEntity> {

    public List<TennisCourtEntity> list() {
        return listAll();
    }

    public Optional<TennisCourtEntity> findById(String id) {
        return find("id", id).firstResultOptional();
    }

    @Transactional
    public TennisCourtEntity create(String name, String country, String city, Surface surface) {
        TennisCourtEntity p = new TennisCourtEntity(name, country, city, surface);
        persist(p);
        return p;
    }
}
