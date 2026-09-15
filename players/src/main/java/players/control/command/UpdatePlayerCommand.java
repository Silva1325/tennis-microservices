package players.control.command;

import java.util.UUID;

public record UpdatePlayerCommand(
        UUID id,
        String email,
        String firstname,
        String lastname,
        String country,
        int age
) {}
