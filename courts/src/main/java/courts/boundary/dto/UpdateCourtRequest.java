package courts.boundary.dto;

import courts.entity.Surface;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateCourtRequest(
        @NotBlank String name,
        @NotBlank String country,
        @NotBlank String city,
        @NotNull Surface surface
) {}
