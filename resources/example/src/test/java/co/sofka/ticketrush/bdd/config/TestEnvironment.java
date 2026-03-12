package co.sofka.ticketrush.bdd.config;

public class TestEnvironment {
    public String uiBaseUrl() {
        return System.getProperty("ui.base.url", "http://localhost:3000");
    }

    public String crudBaseUrl() {
        return System.getProperty("crud.base.url", "http://localhost:8002");
    }

    public String producerBaseUrl() {
        return System.getProperty("producer.base.url", "http://localhost:8001");
    }

    public boolean headless() {
        return Boolean.parseBoolean(System.getProperty("headless", "true"));
    }
}
