package players.control.exception;

import java.util.UUID;

import jakarta.ws.rs.core.Response;

public class PlayerNotFoundException extends BusinessException {
    public PlayerNotFoundException(UUID id) {
        super(Response.Status.NOT_FOUND, "Player not found: " + id);
    }
}
