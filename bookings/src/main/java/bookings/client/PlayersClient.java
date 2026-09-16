package bookings.client;

import java.util.UUID;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/players")
@RegisterRestClient(configKey = "players-api")
public interface PlayersClient {
    @GET
    @Path("{id}")
    PlayerSummary getById(@PathParam("id") UUID id);
}