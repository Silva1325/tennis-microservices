package courts.control.exception;

import jakarta.ws.rs.core.Response;

public class DuplicateCourtException extends BusinessException {
    public DuplicateCourtException(String name, String city) {
        super(Response.Status.CONFLICT, "Tennis court already exists: " + name + " (" + city + ")");
    }
}
