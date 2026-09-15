package players.control.command;

public record CreatePlayerCommand(
        String email,
        String password,
        String firstname,
        String lastname,
        String country,
        int age
) {}
