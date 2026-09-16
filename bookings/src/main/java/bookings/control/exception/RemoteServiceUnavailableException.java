package bookings.control.exception;

import jakarta.ws.rs.core.Response;

public class RemoteServiceUnavailableException extends BusinessException {
    public RemoteServiceUnavailableException(String service) {
        super(Response.Status.SERVICE_UNAVAILABLE, service + " service is unavailable");
    }
}
