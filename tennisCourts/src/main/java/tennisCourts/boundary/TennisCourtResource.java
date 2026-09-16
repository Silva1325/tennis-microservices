package tennisCourts.boundary;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
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
    @Operation(summary = "Create a tennis court")
    @APIResponses({
        @APIResponse(responseCode = "201", description = "Court created"),
        @APIResponse(responseCode = "400", description = "Invalid request body"),
        @APIResponse(responseCode = "409", description = "A court with this name already exists in this city")
    })
    public Response create(@Valid CreateTennisCourtRequest req){
        TennisCourtEntity created = commandService.create(new CreateTennisCourtCommand(req.name(), req.country(), req.city(), req.surface()));
        return Response.created(URI.create("/tennisCourts/" + created.getPublicId()))
                .entity(TennisCourtResponse.from(created))
                .build();
    }

    @GET
    @Operation(summary = "List all tennis courts")
    @APIResponse(responseCode = "200", description = "The tennis courts")
    public List<TennisCourtResponse> list() {
        return queryService.list().stream().map(TennisCourtResponse::from).toList();
    }

    @GET
    @Path("{id}")
    @Operation(summary = "Get a tennis court by id")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "The tennis court"),
        @APIResponse(responseCode = "404", description = "No court with this id")
    })
    public TennisCourtResponse get(@PathParam("id") UUID id) {
        return TennisCourtResponse.from(queryService.findByPublicId(id).orElseThrow(() -> new TennisCourtNotFoundException(id)));
    }

    @PUT
    @Path("{id}")
    @Operation(summary = "Update a tennis court")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Court updated"),
        @APIResponse(responseCode = "400", description = "Invalid request body"),
        @APIResponse(responseCode = "404", description = "No court with this id"),
        @APIResponse(responseCode = "409", description = "Another court already has this name in this city")
    })
    public TennisCourtResponse update(@PathParam("id") UUID id, @Valid UpdateTennisCourtRequest req) {
        TennisCourtEntity updated = commandService.update(new UpdateTennisCourtCommand(id, req.name(), req.country(), req.city(), req.surface()));
        return TennisCourtResponse.from(updated);
    }

    @DELETE
    @Path("{id}")
    @Operation(summary = "Delete a tennis court")
    @APIResponses({
        @APIResponse(responseCode = "204", description = "Court deleted"),
        @APIResponse(responseCode = "404", description = "No court with this id")
    })
    public Response delete(@PathParam("id") UUID id) {
        commandService.delete(new DeleteTennisCourtCommand(id));
        return Response.noContent().build();
    }
}
