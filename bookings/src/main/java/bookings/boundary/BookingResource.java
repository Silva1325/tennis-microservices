package bookings.boundary;

import bookings.boundary.dto.BookingResponse;
import bookings.boundary.dto.CreateBookingRequest;
import bookings.control.BookingCommandService;
import bookings.control.BookingQueryService;
import bookings.control.command.CreateBookingCommand;
import bookings.control.exception.BookingNotFoundException;
import bookings.entity.BookingEntity;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("/bookings")
public class BookingResource {

    @Inject
    BookingCommandService commandService;

    @Inject
    BookingQueryService queryService;

    @POST
    @Operation(summary = "Book a court")
    @APIResponses({
        @APIResponse(responseCode = "201", description = "Booking created"),
        @APIResponse(responseCode = "400", description = "Invalid body, start time not on the hour, or a duplicate participant"),
        @APIResponse(responseCode = "404", description = "Court or a participant does not exist"),
        @APIResponse(responseCode = "409", description = "The court is already booked at that time"),
        @APIResponse(responseCode = "503", description = "The players or courts service is unavailable")
    })
    public Response create(@Valid CreateBookingRequest req) {
        BookingEntity created = commandService.create(new CreateBookingCommand(
            req.courtId(),
            req.startTime(),
            req.participantIds()
        ));
        return Response.created(URI.create("/bookings/" + created.getPublicId()))
            .entity(BookingResponse.from(created))
            .build();
    }

    @GET
    @Operation(summary = "List all bookings")
    @APIResponse(responseCode = "200", description = "The bookings")
    public List<BookingResponse> list() {
        return queryService.list().stream().map(BookingResponse::from).toList();
    }

    @GET
    @Path("{id}")
    @Operation(summary = "Get a booking by id")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "The booking"),
        @APIResponse(responseCode = "404", description = "No booking with this id")
    })
    public BookingResponse get(@PathParam("id") UUID id) {
        return BookingResponse.from(queryService.findByPublicId(id)
                .orElseThrow(() -> new BookingNotFoundException(id)));
    }
}
