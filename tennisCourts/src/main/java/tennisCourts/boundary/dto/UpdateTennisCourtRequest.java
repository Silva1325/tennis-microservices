package tennisCourts.boundary.dto;

import tennisCourts.entity.Surface;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateTennisCourtRequest(
        @NotBlank String name,
        @NotBlank String country,
        @NotBlank String city,
        @NotNull Surface surface
) {}
