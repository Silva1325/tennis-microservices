package players.boundary.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import players.entity.PlayerEntity;

public record PlayerResponse(
    UUID id,
    String email,
    String firstname,
    String lastname,
    String country,
    int age,
    LocalDateTime createDate,
    LocalDateTime updateDate
) {
    public static PlayerResponse from(PlayerEntity entity) {
        return new PlayerResponse(
            entity.getPublicId(),
            entity.getEmail(),
            entity.getFirstname(),
            entity.getLastname(),
            entity.getCountry(),
            entity.getAge(),
            entity.getCreateDate(),
            entity.getUpdateDate()
        );
    }
}
