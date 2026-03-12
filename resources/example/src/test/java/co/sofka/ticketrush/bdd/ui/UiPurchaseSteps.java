package co.sofka.ticketrush.bdd.ui;

import co.sofka.ticketrush.bdd.config.TestContext;
import co.sofka.ticketrush.bdd.config.TestEnvironment;
import co.sofka.ticketrush.bdd.support.TestDataApiClient;
import co.sofka.ticketrush.bdd.ui.port.BrowserAutomation;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import static org.assertj.core.api.Assertions.assertThat;

public class UiPurchaseSteps {
    private final TestContext context;
    private final TestEnvironment environment;
    private final TestDataApiClient testDataApiClient;

    public UiPurchaseSteps(TestContext context, TestEnvironment environment, TestDataApiClient testDataApiClient) {
        this.context = context;
        this.environment = environment;
        this.testDataApiClient = testDataApiClient;
    }

    @Dado("que existe un evento disponible para la venta")
    public void queExisteUnEventoDisponibleParaLaVenta() {
        var data = testDataApiClient.createFutureEventWithAvailableTickets(1);
        context.setEventId(data.eventId());
        context.setTicketId(data.firstTicketId());
        context.setEventName(data.eventName());
    }

    @Dado("que me encuentro en el portal de compra")
    public void queMeEncuentroEnElPortalDeCompra() {
        BrowserAutomation browser = context.getBrowserAutomation();
        browser.navigateTo(environment.uiBaseUrl());
        browser.waitForUrlMatching(".*/buy$");
        browser.waitForText("Compra de Tickets", 10000);
    }

    @Cuando("selecciono el evento para comprar")
    public void seleccionoElEventoParaComprar() {
        BrowserAutomation browser = context.getBrowserAutomation();
        String eventLinkSelector = String.format("a[href='/buy/%d']", context.getEventId());
        browser.waitForElement(eventLinkSelector, 10000);
        browser.click(eventLinkSelector);
        browser.waitForUrlMatching(".*/buy/\\d+$");
        browser.waitForElement("#email", 10000);
    }

    @Cuando("proporciono mi correo electrónico {string}")
    public void proporcionoMiCorreoElectronico(String email) {
        context.getBrowserAutomation().fill("#email", email);
    }

    @Cuando("confirmo la intención de compra")
    public void confirmoLaIntencionDeCompra() {
        context.getBrowserAutomation().click("button[type='submit']");
    }

    @Entonces("el sistema debe permitirme proceder al pago")
    public void elSistemaDebePermitirmeProcederAlPago() {
        BrowserAutomation browser = context.getBrowserAutomation();
        browser.waitForText("Completar Pago", 15000);
        assertThat(browser.getPageSource()).contains("Información de Pago");
    }
}
