package tennisCourts.control.exception;

import jakarta.ws.rs.core.Response;

public class DuplicateTennisCourtException extends BusinessException {
    public DuplicateTennisCourtException(String name, String city) {
        super(Response.Status.CONFLICT, "Tennis court already exists: " + name + " (" + city + ")");
    }
}
