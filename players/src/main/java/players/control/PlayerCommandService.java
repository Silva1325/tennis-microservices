package players.control;

import io.quarkus.elytron.security.common.BcryptUtil;

import players.control.command.CreatePlayerCommand;
import players.control.command.DeletePlayerCommand;
import players.control.command.UpdatePlayerCommand;
import players.control.exception.DuplicateEmailException;
import players.control.exception.PlayerNotFoundException;
import players.entity.PlayerEntity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;


@ApplicationScoped
public class PlayerCommandService {

    @Inject
    PlayerRepository repository;

    @Transactional
    public PlayerEntity create(CreatePlayerCommand command) {
        if (repository.findByEmail(command.email()).isPresent()) {
            throw new DuplicateEmailException(command.email());
        }
        String passwordHash = BcryptUtil.bcryptHash(command.password());
        PlayerEntity player = new PlayerEntity(command.email(), passwordHash, command.firstname(), command.lastname(), command.country(), command.age());
        repository.persist(player);
        return player;
    }

    @Transactional
    public PlayerEntity update(UpdatePlayerCommand command) {
        PlayerEntity player = repository.findByPublicId(command.id())
                .orElseThrow(() -> new PlayerNotFoundException(command.id()));
        boolean emailTaken = repository.findByEmail(command.email())
                .filter(other -> !other.getId().equals(player.getId()))
                .isPresent();
        if (emailTaken) {
            throw new DuplicateEmailException(command.email());
        }
        player.setEmail(command.email());
        player.setFirstname(command.firstname());
        player.setLastname(command.lastname());
        player.setCountry(command.country());
        player.setAge(command.age());
        return player;
    }

    @Transactional
    public void delete(DeletePlayerCommand command) {
        PlayerEntity player = repository.findByPublicId(command.id())
                .orElseThrow(() -> new PlayerNotFoundException(command.id()));
        repository.delete(player);
    }
}
