package tennisCourts.boundary;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import tennisCourts.boundary.dto.CreateTennisCourtRequest;
import tennisCourts.boundary.dto.TennisCourtResponse;
import tennisCourts.boundary.dto.UpdateTennisCourtRequest;
import tennisCourts.control.TennisCourtCommandService;
import tennisCourts.control.TennisCourtQueryService;
import tennisCourts.control.command.CreateTennisCourtCommand;
import tennisCourts.control.command.DeleteTennisCourtCommand;
import tennisCourts.control.command.UpdateTennisCourtCommand;
import tennisCourts.control.exception.TennisCourtNotFoundException;
import tennisCourts.entity.TennisCourtEntity;

import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
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
        TennisCourtEntity created = commandService.create(new CreateTennisCourtCommand(req.name(), req.country(), req.city(), req.surface()));
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

    @PUT
    @Path("{id}")
    public TennisCourtResponse update(@PathParam("id") UUID id, @Valid UpdateTennisCourtRequest req) {
        TennisCourtEntity updated = commandService.update(new UpdateTennisCourtCommand(id, req.name(), req.country(), req.city(), req.surface()));
        return TennisCourtResponse.from(updated);
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") UUID id) {
        commandService.delete(new DeleteTennisCourtCommand(id));
        return Response.noContent().build();
    }
}
