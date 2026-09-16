package courts.boundary.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import courts.entity.Surface;
import courts.entity.CourtEntity;

public record CourtResponse(
    UUID id,
    String name,
    String country,
    String city,
    Surface surface,
    LocalDateTime createDate,
    LocalDateTime updateDate
) {
    public static CourtResponse from(CourtEntity entity) {
        return new CourtResponse(
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
