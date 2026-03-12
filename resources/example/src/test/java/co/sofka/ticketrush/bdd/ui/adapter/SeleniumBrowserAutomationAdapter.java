package co.sofka.ticketrush.bdd.ui.adapter;

import co.sofka.ticketrush.bdd.ui.port.BrowserAutomation;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.regex.Pattern;

public class SeleniumBrowserAutomationAdapter implements BrowserAutomation {
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(15);

    private final WebDriver driver;
    private final WebDriverWait defaultWait;

    public SeleniumBrowserAutomationAdapter(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }

        options.addArguments(
            "--window-size=1440,1080",
            "--disable-dev-shm-usage",
            "--no-sandbox",
            "--remote-allow-origins=*"
        );

        this.driver = new ChromeDriver(options);
        this.defaultWait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
    }

    @Override
    public void navigateTo(String url) {
        driver.get(url);
    }

    @Override
    public void waitForUrlMatching(String regex) {
        defaultWait.until(driver -> Pattern.compile(regex).matcher(driver.getCurrentUrl()).matches());
    }

    @Override
    public void waitForText(String text, long timeoutMs) {
        new WebDriverWait(driver, Duration.ofMillis(timeoutMs))
            .until(ExpectedConditions.visibilityOfElementLocated(byVisibleText(text)));
    }

    @Override
    public void waitForElement(String cssSelector, long timeoutMs) {
        new WebDriverWait(driver, Duration.ofMillis(timeoutMs))
            .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSelector)));
    }

    @Override
    public long countElementsByVisibleText(String text) {
        return driver.findElements(byVisibleText(text)).size();
    }

    @Override
    public void clickFirstElementByVisibleText(String text) {
        List<WebElement> elements = driver.findElements(byVisibleText(text));
        if (elements.isEmpty()) {
            throw new IllegalStateException("No se encontró ningún elemento visible con texto: " + text);
        }
        defaultWait.until(ExpectedConditions.elementToBeClickable(elements.getFirst())).click();
    }

    @Override
    public void fill(String cssSelector, String value) {
        WebElement element = defaultWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSelector)));
        element.clear();
        element.sendKeys(value);
    }

    @Override
    public void click(String cssSelector) {
        defaultWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(cssSelector))).click();
    }

    @Override
    public String getPageSource() {
        return driver.getPageSource();
    }

    @Override
    public void close() {
        driver.quit();
    }

    private By byVisibleText(String text) {
        String escaped = text.replace("'", "\\'");
        return By.xpath("//*[contains(normalize-space(.), '" + escaped + "')]");
    }
}
