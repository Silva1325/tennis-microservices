package players.control.exception;

import jakarta.ws.rs.core.Response;
import lombok.Getter;

@Getter
public abstract class BusinessException extends RuntimeException {
    private final Response.Status status;

    protected BusinessException(Response.Status status, String message) {
        super(message);
        this.status = status;
    }

}
