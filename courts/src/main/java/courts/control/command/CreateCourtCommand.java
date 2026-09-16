package courts.control.command;

import courts.entity.Surface;

public record CreateCourtCommand(
        String name,
        String country,
        String city,
        Surface surface
) {}
