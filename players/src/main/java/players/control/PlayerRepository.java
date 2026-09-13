package players.control;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import players.entity.PlayerEntity;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class PlayerRepository implements PanacheRepository<PlayerEntity> {
    public Optional<PlayerEntity> findByPublicId(UUID publicId) {
        return find("publicId", publicId).firstResultOptional();
    }
}