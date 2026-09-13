package players.control;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import players.entity.PlayerEntity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PlayerQueryService {
    @Inject
    PlayerRepository repository;

    public List<PlayerEntity> list() {
        return repository.listAll();
    }

    public Optional<PlayerEntity> findByPublicId(UUID publicId) {
        return repository.findByPublicId(publicId);
    }
}
