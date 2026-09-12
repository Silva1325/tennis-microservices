package players.control;

import java.util.List;

import players.entity.PlayerEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PlayerService implements PanacheRepository<PlayerEntity> {

    public List<PlayerEntity> list() {
        return listAll();
    }

    public java.util.Optional<PlayerEntity> findById(String id) {
        return find("id", id).firstResultOptional();
    }

    @Transactional
    public PlayerEntity create(String firstname,String lastname,String country, int age){
        PlayerEntity p = new PlayerEntity(firstname,lastname,country,age);
        persist(p);
        return p;
    }


}