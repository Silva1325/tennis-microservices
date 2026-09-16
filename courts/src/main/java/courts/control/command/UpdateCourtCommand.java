package courts.control.command;

import java.util.UUID;

import courts.entity.Surface;

public record UpdateCourtCommand(
        UUID id,
        String name,
        String country,
        String city,
        Surface surface
) {}
