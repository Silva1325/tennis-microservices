package tennisCourts.control.command;

import java.util.UUID;

import tennisCourts.entity.Surface;

public record UpdateTennisCourtCommand(
        UUID id,
        String name,
        String country,
        String city,
        Surface surface
) {}
