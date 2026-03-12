package co.sofka.ticketrush.bdd.ui.port;

public interface BrowserAutomation {
    void navigateTo(String url);

    void waitForUrlMatching(String regex);

    void waitForText(String text, long timeoutMs);

    void waitForElement(String cssSelector, long timeoutMs);

    long countElementsByVisibleText(String text);

    void clickFirstElementByVisibleText(String text);

    void fill(String cssSelector, String value);

    void click(String cssSelector);

    String getPageSource();

    void close();
}
