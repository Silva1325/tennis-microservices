package players.control;

import java.util.List;
import java.util.Optional;

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

    public Optional<PlayerEntity> findById(String id) {
        return repository.find("id", id).firstResultOptional();
    }
}