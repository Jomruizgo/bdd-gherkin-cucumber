package co.sofka.ticketrush.bdd.support;

import co.sofka.ticketrush.bdd.config.TestEnvironment;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class TestDataApiClient {
    private final TestEnvironment environment;

    public TestDataApiClient(TestEnvironment environment) {
        this.environment = environment;
    }

    public TestTicketData createFutureEventWithAvailableTickets(int quantity) {
        String uniqueName = "BDD Demo Event " + System.currentTimeMillis();
        String startsAt = Instant.now().plus(10, ChronoUnit.DAYS).truncatedTo(ChronoUnit.SECONDS).toString();
        String createEventJson = String.format(
            "{\"name\":\"%s\",\"startsAt\":\"%s\"}",
            uniqueName,
            startsAt
        );

        Response eventResponse = given()
            .baseUri(environment.crudBaseUrl())
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(createEventJson)
            .when()
            .post("/api/events")
            .then()
            .extract()
            .response();

        assertThat(eventResponse.statusCode())
            .as("La creación del evento de prueba debe responder 201 o 200")
            .isIn(200, 201);

        long eventId = eventResponse.jsonPath().getLong("id");
        String createTicketsJson = String.format(
            "{\"eventId\":%d,\"quantity\":%d}",
            eventId,
            quantity
        );

        Response ticketsResponse = given()
            .baseUri(environment.crudBaseUrl())
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(createTicketsJson)
            .when()
            .post("/api/tickets/bulk")
            .then()
            .extract()
            .response();

        assertThat(ticketsResponse.statusCode())
            .as("La creación de tickets de prueba debe responder 201 o 200")
            .isIn(200, 201);

        List<Map<String, Object>> tickets = ticketsResponse.jsonPath().getList(".");
        List<Long> ticketIds = tickets.stream()
            .map(ticket -> ticket.get("id"))
            .filter(Objects::nonNull)
            .map(value -> ((Number) value).longValue())
            .toList();

        assertThat(ticketIds)
            .as("Deben existir tickets disponibles para la prueba")
            .isNotNull()
            .isNotEmpty();

        return new TestTicketData(eventId, uniqueName, ticketIds);
    }

    public record TestTicketData(long eventId, String eventName, List<Long> ticketIds) {
        public long firstTicketId() {
            return ticketIds.getFirst();
        }
    }
}
