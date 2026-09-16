package bookings.client;

import java.util.UUID;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/courts")
@RegisterRestClient(configKey = "courts-api")
public interface CourtsClient {

    @GET
    @Path("{id}")
    CourtSummary getById(@PathParam("id") UUID id);
}
