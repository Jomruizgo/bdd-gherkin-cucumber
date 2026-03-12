package co.sofka.ticketrush.bdd.config;

import co.sofka.ticketrush.bdd.ui.port.BrowserAutomation;
import io.restassured.response.Response;

public class TestContext {
    private Long eventId;
    private Long ticketId;
    private String eventName;
    private Response lastResponse;
    private BrowserAutomation browserAutomation;

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Response getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(Response lastResponse) {
        this.lastResponse = lastResponse;
    }

    public BrowserAutomation getBrowserAutomation() {
        return browserAutomation;
    }

    public void setBrowserAutomation(BrowserAutomation browserAutomation) {
        this.browserAutomation = browserAutomation;
    }
}
