package co.sofka.ticketrush.bdd.api;

import co.sofka.ticketrush.bdd.config.TestContext;
import co.sofka.ticketrush.bdd.config.TestEnvironment;
import co.sofka.ticketrush.bdd.support.TestDataApiClient;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class ApiReservationSteps {
    private final TestContext context;
    private final TestEnvironment environment;
    private final TestDataApiClient testDataApiClient;

    public ApiReservationSteps(TestContext context, TestEnvironment environment, TestDataApiClient testDataApiClient) {
        this.context = context;
        this.environment = environment;
        this.testDataApiClient = testDataApiClient;
    }

    @Dado("que existe un evento con tickets disponibles")
    public void queExisteUnEventoConTicketsDisponibles() {
        var data = testDataApiClient.createFutureEventWithAvailableTickets(1);
        context.setEventId(data.eventId());
        context.setTicketId(data.firstTicketId());
        context.setEventName(data.eventName());
    }

    @Cuando("solicito la reserva de un ticket para dicho evento")
    public void solicitoLaReservaDeUnTicketParaDichoEvento() {
        String reserveJson = String.format(
            "{\"eventId\":%d,\"ticketId\":%d,\"orderId\":\"BDD-ORDER-%d\",\"reservedBy\":\"bdd-backend@example.com\",\"expiresInSeconds\":300}",
            context.getEventId(),
            context.getTicketId(),
            System.currentTimeMillis()
        );

        Response response = given()
            .baseUri(environment.producerBaseUrl())
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(reserveJson)
            .when()
            .post("/api/tickets/reserve")
            .then()
            .extract()
            .response();

        context.setLastResponse(response);
    }

    @Entonces("la solicitud de reserva debe ser aceptada")
    public void laSolicitudDeReservaDebeSerAceptada() {
        assertThat(context.getLastResponse().statusCode()).isEqualTo(202);
    }

    @Entonces("el sistema debe confirmar la recepción de la solicitud")
    public void elSistemaDebeConfirmarLaRecepcionDeLaSolicitud() {
        Response response = context.getLastResponse();
        assertThat(response.jsonPath().getLong("ticketId")).isEqualTo(context.getTicketId());
        assertThat(response.jsonPath().getString("message")).isNotBlank();
    }
}
