package tennisCourts.control;

import tennisCourts.entity.TennisCourtEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class TennisCourtRepository implements PanacheRepository<TennisCourtEntity> {
    public Optional<TennisCourtEntity> findByPublicId(UUID publicId){
        return find("publicId",publicId).firstResultOptional();
    }
}