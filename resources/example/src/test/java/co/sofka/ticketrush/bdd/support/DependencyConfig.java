package co.sofka.ticketrush.bdd.support;

import co.sofka.ticketrush.bdd.config.TestContext;
import co.sofka.ticketrush.bdd.config.TestEnvironment;

public class DependencyConfig {
    private final TestEnvironment environment = new TestEnvironment();
    private final TestContext context = new TestContext();
    private final TestDataApiClient testDataApiClient = new TestDataApiClient(environment);

    public TestEnvironment getEnvironment() {
        return environment;
    }

    public TestContext getContext() {
        return context;
    }

    public TestDataApiClient getTestDataApiClient() {
        return testDataApiClient;
    }
}
