package players.control;

import players.entity.PlayerEntity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PlayerCommandService {

    @Inject
    PlayerRepository repository;

    @Transactional
    public PlayerEntity create(String firstname, String lastname, String country, int age) {
        PlayerEntity p = new PlayerEntity(firstname, lastname, country, age);
        repository.persist(p);
        return p;
    }
}