package tennisCourts.boundary.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import tennisCourts.entity.Surface;
import tennisCourts.entity.TennisCourtEntity;

public record TennisCourtResponse(
    UUID id,
    String name,
    String country,
    String city,
    Surface surface,
    LocalDateTime createDate,
    LocalDateTime updateDate
) {
    public static TennisCourtResponse from(TennisCourtEntity entity) {
        return new TennisCourtResponse(
            entity.getPublicId(),
            entity.getName(),
            entity.getCountry(),
            entity.getCity(),
            entity.getSurface(),
            entity.getCreateDate(),
            entity.getUpdateDate()
        );
    }
}
