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

import static org.junit.jupiter.api.Assertions.*;

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

        driver.get("http://localhost:8080/login");

    }

    @Test
    public void testLeereEingabefelder() {
        System.out.println("Test: Empty input fields.");

        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("vaadin-button:nth-child(2)")));
        assertEquals("Anmelden", loginButton.getText(), "Login button not found or incorrect text.");
        loginButton.click();
        System.out.println("Clicked the login button.");

        // error meddage
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fehlermeldung")));
        assertEquals("Incorrect username or password. Check that you have entered the correct username and password and try again.", errorMessage.getText(), "Error message does not match the expected value.");
        System.out.println("Verified error message: " + errorMessage.getText());

        // Pause for observation
        try {
            Thread.sleep(3000); // Wait for 3 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUngueltigerBenutzername() {
        System.out.println("Test: Invalid username.");

        // falscher Benutzer und passwort
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("input-vaadin-text-field-7")));
        usernameField.sendKeys("benutzer@name@domain.com");
        System.out.println("Entered invalid username.");

        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("input-vaadin-password-field-8")));
        passwordField.sendKeys("gueltigesPasswort");
        System.out.println("Entered valid password.");

        // Click the login button directly
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("vaadin-button:nth-child(2)")));
        assertEquals("Anmelden", loginButton.getText(), "Login button not found or incorrect text.");
        loginButton.click();
        System.out.println("Clicked the login button.");

        // Error message
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fehlermeldung")));
        assertEquals("Incorrect username or password. Check that you have entered the correct username and password and try again.", errorMessage.getText(), "Error message does not match the expected value.");
        System.out.println("Verified error message: " + errorMessage.getText());

        // Wartezeit
        try {
            Thread.sleep(3000); // Wait for 3 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    @AfterEach
    public void tearDown() {
        // Browser schließen
        if (driver != null) {
            driver.quit();
        }
    }

}