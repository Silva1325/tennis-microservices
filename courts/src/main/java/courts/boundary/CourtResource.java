package courts.boundary;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import courts.boundary.dto.CreateCourtRequest;
import courts.boundary.dto.CourtResponse;
import courts.boundary.dto.UpdateCourtRequest;
import courts.control.CourtCommandService;
import courts.control.CourtQueryService;
import courts.control.command.CreateCourtCommand;
import courts.control.command.DeleteCourtCommand;
import courts.control.command.UpdateCourtCommand;
import courts.control.exception.CourtNotFoundException;
import courts.entity.CourtEntity;

import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path("/courts")
public class CourtResource {

    @Inject
    CourtCommandService commandService;
    @Inject
    CourtQueryService queryService;

    @POST
    @Operation(summary = "Create a tennis court")
    @APIResponses({
        @APIResponse(responseCode = "201", description = "Court created"),
        @APIResponse(responseCode = "400", description = "Invalid request body"),
        @APIResponse(responseCode = "409", description = "A court with this name already exists in this city")
    })
    public Response create(@Valid CreateCourtRequest req){
        CourtEntity created = commandService.create(new CreateCourtCommand(req.name(), req.country(), req.city(), req.surface()));
        return Response.created(URI.create("/courts/" + created.getPublicId()))
                .entity(CourtResponse.from(created))
                .build();
    }

    @GET
    @Operation(summary = "List all tennis courts")
    @APIResponse(responseCode = "200", description = "The tennis courts")
    public List<CourtResponse> list() {
        return queryService.list().stream().map(CourtResponse::from).toList();
    }

    @GET
    @Path("{id}")
    @Operation(summary = "Get a tennis court by id")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "The tennis court"),
        @APIResponse(responseCode = "404", description = "No court with this id")
    })
    public CourtResponse get(@PathParam("id") UUID id) {
        return CourtResponse.from(queryService.findByPublicId(id).orElseThrow(() -> new CourtNotFoundException(id)));
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
    public CourtResponse update(@PathParam("id") UUID id, @Valid UpdateCourtRequest req) {
        CourtEntity updated = commandService.update(new UpdateCourtCommand(id, req.name(), req.country(), req.city(), req.surface()));
        return CourtResponse.from(updated);
    }

    @DELETE
    @Path("{id}")
    @Operation(summary = "Delete a tennis court")
    @APIResponses({
        @APIResponse(responseCode = "204", description = "Court deleted"),
        @APIResponse(responseCode = "404", description = "No court with this id")
    })
    public Response delete(@PathParam("id") UUID id) {
        commandService.delete(new DeleteCourtCommand(id));
        return Response.noContent().build();
    }
}
