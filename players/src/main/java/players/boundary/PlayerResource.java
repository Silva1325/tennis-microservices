package players.boundary;

import java.net.URI;
import java.util.List;

import players.control.PlayerService;
import players.entity.PlayerEntity;

import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/players")
public class PlayerResource {

    @Inject
    PlayerService service;

    public record CreatePlayer(String firstname,String lastname,String country, int age) { }

    @POST
    public Response create(CreatePlayer req){
        if(req == null || req.firstname() == null || req.lastname() == null || req.country() == null){
            throw new BadRequestException("Bad Request");
        }
        PlayerEntity created = service.create(req.firstname,req.lastname,req.country,req.age);
        return Response.created(URI.create("/players/" + created.getId()))
                .entity(created)
                .build();
    }

    @GET
    public List<PlayerEntity> list() {
        return service.list();
    }

    @GET
    @Path("{id}")
    public PlayerEntity get(@PathParam("id") String id) {
        return service.findById(id).orElseThrow(NotFoundException::new);
    }
}
