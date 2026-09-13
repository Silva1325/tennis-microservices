package players.boundary.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;

public record CreatePlayerRequest(
        @NotBlank String firstname,
        @NotBlank String lastname,
        @NotBlank String country,
        @Min(0) int age
) {}