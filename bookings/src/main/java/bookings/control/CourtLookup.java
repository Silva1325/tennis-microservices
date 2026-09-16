package bookings.control;

import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import bookings.client.CourtSummary;
import bookings.client.CourtsClient;
import bookings.control.exception.CourtNotFoundException;
import bookings.control.exception.RemoteServiceUnavailableException;

@ApplicationScoped
public class CourtLookup {

    @Inject
    @RestClient
    CourtsClient client;

    public CourtSummary requireExists(UUID courtId) {
        try {
            return client.getById(courtId);
        } catch (WebApplicationException e) {
            if (e.getResponse().getStatus() == 404) {
                throw new CourtNotFoundException(courtId);
            }
            throw new RemoteServiceUnavailableException("Courts");
        } catch (ProcessingException e) {
            throw new RemoteServiceUnavailableException("Courts");
        }
    }
}
