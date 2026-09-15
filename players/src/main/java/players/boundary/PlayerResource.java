package players.boundary;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
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
    public Response create(@Valid CreatePlayerRequest req){
        PlayerEntity created = commandService.create(new CreatePlayerCommand(req.email(), req.password(), req.firstname(), req.lastname(), req.country(), req.age()));
        return Response.created(URI.create("/players/" + created.getPublicId()))
                .entity(PlayerResponse.from(created))
                .build();
    }

    @GET
    public List<PlayerResponse> list() {
        return queryService.list().stream().map(PlayerResponse::from).toList();
    }

    @GET
    @Path("{id}")
    public PlayerResponse get(@PathParam("id") UUID id) {
        return PlayerResponse.from(queryService.findByPublicId(id).orElseThrow(() -> new PlayerNotFoundException(id)));
    }

    @PUT
    @Path("{id}")
    public PlayerResponse update(@PathParam("id") UUID id, @Valid UpdatePlayerRequest req) {
        PlayerEntity updated = commandService.update(new UpdatePlayerCommand(id, req.email(), req.firstname(), req.lastname(), req.country(), req.age()));
        return PlayerResponse.from(updated);
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") UUID id) {
        commandService.delete(new DeletePlayerCommand(id));
        return Response.noContent().build();
    }
}
