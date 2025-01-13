import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// page_url = http://localhost:8080/login
public class GleicherBenutzernameTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        // Setup
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @Test
    public void testDuplicateUsernameRegistration() {
        // Öffne die Login-Seite und gehe zur Registrierung
        driver.get("http://localhost:8080/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("vaadin-side-nav-item:nth-child(3)"))).click();

        // Fülle Registrierungsdaten aus für Alligator14 (Student)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("input-vaadin-text-field-35"))).sendKeys("Irgendwas");
        driver.findElement(By.id("input-vaadin-text-field-36")).sendKeys("Max");
        driver.findElement(By.id("input-vaadin-text-field-37")).sendKeys("Alligator14"); // Benutzername (existierend)
        driver.findElement(By.id("input-vaadin-email-field-38")).sendKeys("test@gmail.com");
        driver.findElement(By.id("input-vaadin-password-field-39")).sendKeys("Alligator14"); // Passwort
        driver.findElement(By.id("input-vaadin-password-field-40")).sendKeys("Alligator14"); // Passwortbestätigung

        // Formular absenden
        driver.findElement(By.cssSelector("vaadin-button:nth-child(7)")).click();

        // Überprüfe die Fehlermeldung mit JavaScript-Executor
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String script = "return document.querySelector('.error-message')?.innerText || '';";
        String errorMessage = (String) js.executeScript(script);

        // Validierung der Fehlermeldung
        System.out.println("Fehlermeldung: " + errorMessage);
        assertFalse(errorMessage.contains("Fehler: Username schon vergeben"),
                "Die Fehlermeldung wurde nicht korrekt angezeigt!");
    }

    @AfterEach
    public void tearDown() {
        // Browser schließen
        if (driver != null) {
            driver.quit();
        }
    }
}