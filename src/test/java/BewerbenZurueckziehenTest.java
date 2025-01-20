import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

// page_url = http://localhost:8080/login
public class BewerbenZurueckziehenTest {
    private WebDriver driver;
    private WebDriverWait wait;

    // versucht mehrmals zu klicken, nach 3 attempts wirft fehler
    public void ClickButton(By locator) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                driver.findElement(locator).click();
                return;
                // Erfolgreich geklickt
            } catch (StaleElementReferenceException e) {
                System.out.println("Attempt " + (attempts + 1) + ": Element became stale. Retrying...");
                attempts++;
                wait.until(ExpectedConditions.refreshed(
                        ExpectedConditions.elementToBeClickable(locator)
                ));
            }
        }
        throw new RuntimeException("Failed to click element after multiple attempts: " + locator);
    }

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @Test
    public void BewerbungSchreibenZurueckziehen() {
        // Open /login
        driver.get("http://localhost:8080/login");
        // Log in mit Alligator120 (Student)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("input-vaadin-text-field-7"))).sendKeys("Alligator120");
        driver.findElement(By.id("input-vaadin-password-field-8")).sendKeys("Alligator120");
        driver.findElement(By.cssSelector("vaadin-button:nth-child(2)")).click();

        // job suche und bewerben
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Job suchen"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("vaadin-vertical-layout:nth-child(1) > vaadin-horizontal-layout > vaadin-button"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("vaadin-horizontal-layout:nth-child(7) > vaadin-button:nth-child(1)"))).click();

        // klick mit ClickButton, weil es sonst nicht klappt
        ClickButton(By.cssSelector("#overlay > vaadin-vertical-layout > vaadin-button"));


        // zurückziehen unter Bewerbungen
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Meine Bewerbungen"))).click();

        System.out.println("Suche den 'Zurückziehen'-Button...");
        By withdrawButtonLocator = By.xpath("//vaadin-button[contains(text(), 'zurückziehen')]");
        WebElement withdrawButton = wait.until(ExpectedConditions.visibilityOfElementLocated(withdrawButtonLocator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", withdrawButton);
        System.out.println("'Zurückziehen'-Button wurde geklickt.");

        // Popup Warten auf den "Ja"-Button und klicken
        System.out.println("Warten auf das Popup mit 'Ja'-Button...");
        By popupYesButtonLocator = By.xpath("//vaadin-button[contains(text(), 'Ja')]");
        WebElement yesButton = wait.until(ExpectedConditions.visibilityOfElementLocated(popupYesButtonLocator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", yesButton);
        System.out.println("Popup bestätigt mit 'Ja'.");
    }

    //Schließen vom Test
    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}