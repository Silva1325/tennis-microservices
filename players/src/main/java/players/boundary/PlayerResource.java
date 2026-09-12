package players.boundary;

import java.net.URI;
import java.util.List;

import players.boundary.dto.CreatePlayerRequest;
import players.boundary.dto.PlayerResponse;
import players.control.PlayerQueryService;
import players.control.PlayerCommandService;
import players.entity.PlayerEntity;

import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
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
    public Response create(CreatePlayerRequest req){
        if(req == null || req.firstname() == null || req.lastname() == null || req.country() == null){
            throw new BadRequestException("Bad Request");
        }
        PlayerEntity created = commandService.create(req.firstname(), req.lastname(), req.country(), req.age());
        return Response.created(URI.create("/players/" + created.getId()))
                .entity(PlayerResponse.from(created))
                .build();
    }

    @GET
    public List<PlayerResponse> list() {
        return queryService.list().stream().map(PlayerResponse::from).toList();
    }

    @GET
    @Path("{id}")
    public PlayerResponse get(@PathParam("id") String id) {
        return PlayerResponse.from(queryService.findById(id).orElseThrow(NotFoundException::new));
    }
}
