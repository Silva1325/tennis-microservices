package players.control.exception;

import jakarta.ws.rs.core.Response;

public class DuplicateEmailException extends BusinessException {
    public DuplicateEmailException(String email) {
        super(Response.Status.CONFLICT, "Email already registered: " + email);
    }
}
