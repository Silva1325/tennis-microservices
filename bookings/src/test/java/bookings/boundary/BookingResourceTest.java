package bookings.boundary;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bookings.client.CourtSummary;
import bookings.client.CourtsClient;
import bookings.client.PlayerSummary;
import bookings.client.PlayersClient;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class BookingResourceTest {

    @InjectMock
    @RestClient
    PlayersClient playersClient;

    @InjectMock
    @RestClient
    CourtsClient courtsClient;

    // Quarkus resets mocks after every test, so the "both services are up" stubs are set before each one
    @BeforeEach
    void bothServicesAreUp() {
        when(courtsClient.getById(any()))
                .thenAnswer(call -> new CourtSummary(call.getArgument(0), "Clube Tenis Porto", "Porto"));
        when(playersClient.getById(any()))
                .thenAnswer(call -> new PlayerSummary(call.getArgument(0), "Rafael", "Nadal"));
    }

    // a random future slot on the hour, so tests never collide with each other's bookings
    private Instant futureSlot() {
        int daysAhead = ThreadLocalRandom.current().nextInt(1, 3650);
        return Instant.now().plus(daysAhead, ChronoUnit.DAYS).truncatedTo(ChronoUnit.HOURS);
    }

    private String bookingJson(UUID courtId, Instant startTime, UUID... participantIds) {
        String ids = Arrays.stream(participantIds)
                .map(id -> "\"" + id + "\"")
                .collect(Collectors.joining(","));
        return """
                {"courtId":"%s","startTime":"%s","participantIds":[%s]}
                """.formatted(courtId, startTime, ids);
    }

    @Test
    void createsABooking() {
        UUID courtId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();

        given()
            .contentType("application/json")
            .body(bookingJson(courtId, futureSlot(), playerId))
        .when()
            .post("/bookings")
        .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("courtId", is(courtId.toString()))
            .body("courtName", is("Clube Tenis Porto"))
            .body("participants[0].id", is(playerId.toString()))
            .body("participants[0].name", is("Rafael Nadal"));
    }

    @Test
    void rejectsASlotThatIsAlreadyTaken() {
        String body = bookingJson(UUID.randomUUID(), futureSlot(), UUID.randomUUID());

        given()
            .contentType("application/json")
            .body(body)
        .when()
            .post("/bookings")
        .then()
            .statusCode(201);

        given()
            .contentType("application/json")
            .body(body)
        .when()
            .post("/bookings")
        .then()
            .statusCode(409)
            .body("message", notNullValue());
    }

    @Test
    void rejectsADuplicateParticipant() {
        UUID playerId = UUID.randomUUID();

        given()
            .contentType("application/json")
            .body(bookingJson(UUID.randomUUID(), futureSlot(), playerId, playerId))
        .when()
            .post("/bookings")
        .then()
            .statusCode(400)
            .body("message", notNullValue());
    }

    @Test
    void rejectsAStartTimeNotOnTheHour() {
        Instant halfPast = futureSlot().plus(30, ChronoUnit.MINUTES);

        given()
            .contentType("application/json")
            .body(bookingJson(UUID.randomUUID(), halfPast, UUID.randomUUID()))
        .when()
            .post("/bookings")
        .then()
            .statusCode(400)
            .body("message", notNullValue());
    }

    @Test
    void rejectsAStartTimeInThePast() {
        Instant yesterday = Instant.now().minus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.HOURS);

        given()
            .contentType("application/json")
            .body(bookingJson(UUID.randomUUID(), yesterday, UUID.randomUUID()))
        .when()
            .post("/bookings")
        .then()
            .statusCode(400);
    }

    @Test
    void rejectsMoreThanFourParticipants() {
        given()
            .contentType("application/json")
            .body(bookingJson(UUID.randomUUID(), futureSlot(),
                    UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()))
        .when()
            .post("/bookings")
        .then()
            .statusCode(400);
    }

    @Test
    void rejectsABookingWithoutParticipants() {
        given()
            .contentType("application/json")
            .body(bookingJson(UUID.randomUUID(), futureSlot()))
        .when()
            .post("/bookings")
        .then()
            .statusCode(400);
    }

    @Test
    void returns404WhenTheCourtDoesNotExist() {
        when(courtsClient.getById(any())).thenThrow(new WebApplicationException(404));

        given()
            .contentType("application/json")
            .body(bookingJson(UUID.randomUUID(), futureSlot(), UUID.randomUUID()))
        .when()
            .post("/bookings")
        .then()
            .statusCode(404)
            .body("message", notNullValue());
    }

    @Test
    void returns404WhenAPlayerDoesNotExist() {
        when(playersClient.getById(any())).thenThrow(new WebApplicationException(404));

        given()
            .contentType("application/json")
            .body(bookingJson(UUID.randomUUID(), futureSlot(), UUID.randomUUID()))
        .when()
            .post("/bookings")
        .then()
            .statusCode(404)
            .body("message", notNullValue());
    }

    @Test
    void returns503WhenThePlayersServiceIsDown() {
        when(playersClient.getById(any())).thenThrow(new ProcessingException("Connection refused"));

        given()
            .contentType("application/json")
            .body(bookingJson(UUID.randomUUID(), futureSlot(), UUID.randomUUID()))
        .when()
            .post("/bookings")
        .then()
            .statusCode(503)
            .body("message", is("Players service is unavailable"));
    }

    @Test
    void returns503WhenTheCourtsServiceIsDown() {
        when(courtsClient.getById(any())).thenThrow(new ProcessingException("Connection refused"));

        given()
            .contentType("application/json")
            .body(bookingJson(UUID.randomUUID(), futureSlot(), UUID.randomUUID()))
        .when()
            .post("/bookings")
        .then()
            .statusCode(503)
            .body("message", is("Courts service is unavailable"));
    }

    @Test
    void returns503WhenAServiceAnswersWithAServerError() {
        when(playersClient.getById(any())).thenThrow(new WebApplicationException(500));

        given()
            .contentType("application/json")
            .body(bookingJson(UUID.randomUUID(), futureSlot(), UUID.randomUUID()))
        .when()
            .post("/bookings")
        .then()
            .statusCode(503);
    }

    @Test
    void getsABookingById() {
        String id = given()
            .contentType("application/json")
            .body(bookingJson(UUID.randomUUID(), futureSlot(), UUID.randomUUID()))
        .when()
            .post("/bookings")
        .then()
            .statusCode(201)
            .extract().path("id");

        given()
        .when()
            .get("/bookings/" + id)
        .then()
            .statusCode(200)
            .body("id", is(id));
    }

    @Test
    void returns404ForAnUnknownBooking() {
        given()
        .when()
            .get("/bookings/" + UUID.randomUUID())
        .then()
            .statusCode(404)
            .body("message", notNullValue());
    }
}
