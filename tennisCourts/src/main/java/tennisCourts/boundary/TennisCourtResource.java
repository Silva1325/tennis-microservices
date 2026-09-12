package tennisCourts.boundary;

import java.net.URI;
import java.util.List;

import tennisCourts.control.TennisCourtService;
import tennisCourts.entity.Surface;
import tennisCourts.entity.TennisCourtEntity;

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

@Path("/tennisCourts")
public class TennisCourtResource {

    @Inject
    TennisCourtService service;

    public record CreateTennisCourt(String name, String country, String city, Surface surface) { }

    @POST
    public Response create(CreateTennisCourt req){
        if(req == null || req.name() == null || req.country() == null || req.city() == null || req.surface() == null){
            throw new BadRequestException("Bad Request");
        }
        TennisCourtEntity created = service.create(req.name, req.country, req.city, req.surface);
        return Response.created(URI.create("/tennisCourts/" + created.getId()))
                .entity(created)
                .build();
    }

    @GET
    public List<TennisCourtEntity> list() {
        return service.list();
    }

    @GET
    @Path("{id}")
    public TennisCourtEntity get(@PathParam("id") String id) {
        return service.findById(id).orElseThrow(NotFoundException::new);
    }
}