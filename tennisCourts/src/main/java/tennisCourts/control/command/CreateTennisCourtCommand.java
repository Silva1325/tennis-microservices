package tennisCourts.control.command;

import tennisCourts.entity.Surface;

public record CreateTennisCourtCommand(
        String name,
        String country,
        String city,
        Surface surface
) {}
