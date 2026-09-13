package tennisCourts.boundary;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import tennisCourts.boundary.dto.CreateTennisCourtRequest;
import tennisCourts.boundary.dto.TennisCourtResponse;
import tennisCourts.control.TennisCourtCommandService;
import tennisCourts.control.TennisCourtQueryService;
import tennisCourts.control.exception.TennisCourtNotFoundException;
import tennisCourts.entity.TennisCourtEntity;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
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
    public Response create(@Valid CreateTennisCourtRequest req){
        TennisCourtEntity created = commandService.create(req.name(), req.country(), req.city(), req.surface());
        return Response.created(URI.create("/tennisCourts/" + created.getPublicId()))
                .entity(TennisCourtResponse.from(created))
                .build();
    }

    @GET
    public List<TennisCourtResponse> list() {
        return queryService.list().stream().map(TennisCourtResponse::from).toList();
    }

    @GET
    @Path("{id}")
    public TennisCourtResponse get(@PathParam("id") UUID id) {
        return TennisCourtResponse.from(queryService.findByPublicId(id).orElseThrow(() -> new TennisCourtNotFoundException(id)));
    }
}
