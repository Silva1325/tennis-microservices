package courts.control;

import courts.entity.CourtEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CourtRepository implements PanacheRepository<CourtEntity> {
    public Optional<CourtEntity> findByPublicId(UUID publicId){
        return find("publicId", publicId).firstResultOptional();
    }

    public Optional<CourtEntity> findByNameAndCity(String name, String city){
        return find("name = ?1 and city = ?2", name, city).firstResultOptional();
    }
}
