package co.sofka.ticketrush.bdd.ui;

import co.sofka.ticketrush.bdd.config.TestContext;
import co.sofka.ticketrush.bdd.config.TestEnvironment;
import co.sofka.ticketrush.bdd.ui.adapter.SeleniumBrowserAutomationAdapter;
import co.sofka.ticketrush.bdd.ui.port.BrowserAutomation;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class UiHooks {
    private final TestContext context;
    private final TestEnvironment environment;

    public UiHooks(TestContext context, TestEnvironment environment) {
        this.context = context;
        this.environment = environment;
    }

    @Before("@ui")
    public void setUpBrowser() {
        BrowserAutomation browserAutomation = new SeleniumBrowserAutomationAdapter(environment.headless());
        context.setBrowserAutomation(browserAutomation);
    }

    @After("@ui")
    public void tearDownBrowser() {
        if (context.getBrowserAutomation() != null) {
            context.getBrowserAutomation().close();
        }
    }
}
