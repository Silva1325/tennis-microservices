package players.boundary;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import players.boundary.dto.CreatePlayerRequest;
import players.boundary.dto.PlayerResponse;
import players.boundary.dto.UpdatePlayerRequest;
import players.control.PlayerQueryService;
import players.control.PlayerCommandService;
import players.control.command.CreatePlayerCommand;
import players.control.command.DeletePlayerCommand;
import players.control.command.UpdatePlayerCommand;
import players.control.exception.PlayerNotFoundException;
import players.entity.PlayerEntity;

import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path("/players")
public class PlayerResource {

    @Inject
    PlayerCommandService commandService;
    @Inject
    PlayerQueryService queryService;

    @POST
    @Operation(summary = "Create a player")
    @APIResponses({
        @APIResponse(responseCode = "201", description = "Player created"),
        @APIResponse(responseCode = "400", description = "Invalid request body"),
        @APIResponse(responseCode = "409", description = "This email is already registered")
    })
    public Response create(@Valid CreatePlayerRequest req){
        PlayerEntity created = commandService.create(new CreatePlayerCommand(req.email(), req.password(), req.firstname(), req.lastname(), req.country(), req.age()));
        return Response.created(URI.create("/players/" + created.getPublicId()))
                .entity(PlayerResponse.from(created))
                .build();
    }

    @GET
    @Operation(summary = "List all players")
    @APIResponse(responseCode = "200", description = "The players")
    public List<PlayerResponse> list() {
        return queryService.list().stream().map(PlayerResponse::from).toList();
    }

    @GET
    @Path("{id}")
    @Operation(summary = "Get a player by id")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "The player"),
        @APIResponse(responseCode = "404", description = "No player with this id")
    })
    public PlayerResponse get(@PathParam("id") UUID id) {
        return PlayerResponse.from(queryService.findByPublicId(id).orElseThrow(() -> new PlayerNotFoundException(id)));
    }

    @PUT
    @Path("{id}")
    @Operation(summary = "Update a player", description = "Does not change the password.")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Player updated"),
        @APIResponse(responseCode = "400", description = "Invalid request body"),
        @APIResponse(responseCode = "404", description = "No player with this id"),
        @APIResponse(responseCode = "409", description = "This email belongs to another player")
    })
    public PlayerResponse update(@PathParam("id") UUID id, @Valid UpdatePlayerRequest req) {
        PlayerEntity updated = commandService.update(new UpdatePlayerCommand(id, req.email(), req.firstname(), req.lastname(), req.country(), req.age()));
        return PlayerResponse.from(updated);
    }

    @DELETE
    @Path("{id}")
    @Operation(summary = "Delete a player")
    @APIResponses({
        @APIResponse(responseCode = "204", description = "Player deleted"),
        @APIResponse(responseCode = "404", description = "No player with this id")
    })
    public Response delete(@PathParam("id") UUID id) {
        commandService.delete(new DeletePlayerCommand(id));
        return Response.noContent().build();
    }
}
