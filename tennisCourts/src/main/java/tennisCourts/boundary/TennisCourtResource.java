package tennisCourts.boundary;

import java.net.URI;
import java.util.List;

import tennisCourts.boundary.dto.CreateTennisCourtRequest;
import tennisCourts.boundary.dto.TennisCourtResponse;
import tennisCourts.control.TennisCourtCommandService;
import tennisCourts.control.TennisCourtQueryService;
import tennisCourts.entity.TennisCourtEntity;

import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path("/tennisCourts")
public class TennisCourtResource {

    @Inject
    TennisCourtCommandService commandService;
    @Inject
    TennisCourtQueryService queryService;

    @POST
    public Response create(CreateTennisCourtRequest req){
        if(req == null || req.name() == null || req.country() == null || req.city() == null || req.surface() == null){
            throw new BadRequestException("Bad Request");
        }
        TennisCourtEntity created = commandService.create(req.name(), req.country(), req.city(), req.surface());
        return Response.created(URI.create("/tennisCourts/" + created.getId()))
                .entity(TennisCourtResponse.from(created))
                .build();
    }

    @GET
    public List<TennisCourtResponse> list() {
        return queryService.list().stream().map(TennisCourtResponse::from).toList();
    }

    @GET
    @Path("{id}")
    public TennisCourtResponse get(@PathParam("id") String id) {
        return TennisCourtResponse.from(queryService.findById(id).orElseThrow(NotFoundException::new));
    }
}
