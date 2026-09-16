package bookings.control;

import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import bookings.client.PlayerSummary;
import bookings.client.PlayersClient;
import bookings.control.exception.ParticipantNotFoundException;
import bookings.control.exception.RemoteServiceUnavailableException;

@ApplicationScoped
public class PlayerLookup {

    @Inject
    @RestClient
    PlayersClient client;

    public PlayerSummary requireExists(UUID playerId) {
        try {
            return client.getById(playerId);
        } catch (WebApplicationException e) {
            if (e.getResponse().getStatus() == 404) {
                throw new ParticipantNotFoundException(playerId);
            }
            throw new RemoteServiceUnavailableException("Players");
        } catch (ProcessingException e) {
            throw new RemoteServiceUnavailableException("Players");
        }
    }
}
