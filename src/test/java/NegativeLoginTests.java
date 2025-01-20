import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NegativeLoginTests {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        // Setup WebDriver
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // login
        driver.get("http://localhost:8080/");
        System.out.println("Navigated to the login page.");
    }


    @Test
    public void testLeereEingabefelder() {
        System.out.println("Test: Empty input fields.");

        WebElement loginButton = driver.findElement(By.cssSelector("vaadin-button:nth-child(2)"));
        assertEquals("Anmelden", loginButton.getText(), "Login button not found or incorrect text.");
        loginButton.click();
        System.out.println("Clicked the login button.");

        // Error message
        WebElement errorMessage = driver.findElement(By.id("fehlermeldung"));
        assertEquals("Benutzername und Passwort sind erforderlich.", errorMessage.getText(), "Error message does not match the expected value.");
        System.out.println("Verified error message: " + errorMessage.getText());
    }

    @Test
    public void testUngueltigerBenutzername() {
        System.out.println("Test: Invalid username.");

        // Incorrectes Benutzer
        driver.findElement(By.id("input-vaadin-text-field-7")).sendKeys("benutzer@name@domain.com");
        System.out.println("Entered invalid username.");

        driver.findElement(By.id("input-vaadin-password-field-8")).sendKeys("gueltigesPasswort");
        System.out.println("Entered valid password.");

        // klick anmelden
        WebElement loginButton = driver.findElement(By.cssSelector("vaadin-button:nth-child(2)"));
        assertEquals("Anmelden", loginButton.getText(), "Login button not found or incorrect text.");
        loginButton.click();
        System.out.println("Clicked the login button.");

        // Verify the error message
        WebElement errorMessage = driver.findElement(By.id("fehlermeldung"));
        assertEquals("Ungültiger Benutzername.", errorMessage.getText(), "Error message does not match the expected value.");
        System.out.println("Verified error message: " + errorMessage.getText());
    }

    @Test
    public void testUngueltigesPasswort() {
        System.out.println("Test: Invalid password.");

        // richtiger Benutzer und falsches password
        driver.findElement(By.id("input-vaadin-text-field-7")).sendKeys("gueltigerBenutzername");
        System.out.println("Entered valid username.");

        driver.findElement(By.id("input-vaadin-password-field-8")).sendKeys("123");
        System.out.println("Entered invalid password.");

        // login
        WebElement loginButton = driver.findElement(By.cssSelector("vaadin-button:nth-child(2)"));
        assertEquals("Anmelden", loginButton.getText(), "Login button not found or incorrect text.");
        loginButton.click();
        System.out.println("Clicked the login button.");

        // error message
        WebElement errorMessage = driver.findElement(By.id("fehlermeldung"));
        assertEquals("Passwort muss mindestens 6 Zeichen lang sein.", errorMessage.getText(), "Error message does not match the expected value.");
        System.out.println("Verified error message: " + errorMessage.getText());
    }


    @AfterEach
    public void tearDown() {
        // Browser schließen
        if (driver != null) {
            driver.quit();
        }
    }
}