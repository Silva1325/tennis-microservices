package players.control;

import players.control.exception.DuplicateEmailException;
import players.entity.PlayerEntity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PlayerCommandService {

    @Inject
    PlayerRepository repository;

    @Transactional
    public PlayerEntity create(String email, String password, String firstname, String lastname, String country, int age) {
        if (repository.findByEmail(email).isPresent()) {
            throw new DuplicateEmailException(email);
        }
        PlayerEntity player = new PlayerEntity(email, password, firstname, lastname, country, age);
        repository.persist(player);
        return player;
    }
}
