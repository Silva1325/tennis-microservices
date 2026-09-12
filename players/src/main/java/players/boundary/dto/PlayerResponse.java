package players.boundary.dto;

import java.time.LocalDateTime;

import players.entity.PlayerEntity;

public record PlayerResponse(
    Long id,
    String firstname,
    String lastname,
    String country,
    int age,
    LocalDateTime createDate,
    LocalDateTime updateDate
) {
    public static PlayerResponse from(PlayerEntity entity) {
        return new PlayerResponse(
            entity.getId(),
            entity.getFirstname(),
            entity.getLastname(),
            entity.getCountry(),
            entity.getAge(),
            entity.getCreateDate(),
            entity.getUpdateDate()
        );
    }
}
