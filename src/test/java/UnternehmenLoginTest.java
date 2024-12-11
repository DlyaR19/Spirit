import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertTrue;

// page_url = http://localhost:8080/login
public class UnternehmenLoginTest {
    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void testUnternehmenLoginAndProfilePage() {
        //Öffne /Login
        driver.get("http://localhost:8080/login");

        //Wartezeit
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        //Benutzername-Feld
        WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("input-vaadin-text-field-7")));
        usernameField.sendKeys("Alligator13");

        //Passwort-Feld
        WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[type='password']")));
        passwordField.sendKeys("Alligator13");

        //Login-Button
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("vaadin-button[slot='submit']")));
        loginButton.click();

        //Überprüfen, ob die Login-Seite erfolgreich war und wir auf profil sind
        WebElement greetingText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h2")));
        assertTrue(greetingText.getText().contains("Hallo"), "Login fehlgeschlagen!");
    }

    // Browser schließen
    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}