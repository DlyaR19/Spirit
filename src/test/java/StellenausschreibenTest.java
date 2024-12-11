import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class StellenausschreibenTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @Test
    public void createAndDeleteJobPost() {
        //Login
        driver.get("http://localhost:8080/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("input-vaadin-text-field-7"))).sendKeys("Alligator13");
        driver.findElement(By.id("input-vaadin-password-field-8")).sendKeys("Alligator13");
        driver.findElement(By.cssSelector("vaadin-button:nth-child(2)")).click();

        //Stellenausschreibung hinzufügen
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Stellenausschreibung hinzufügen"))).click();
        driver.findElement(By.id("input-vaadin-text-field-12")).sendKeys("Job description");
        driver.findElement(By.id("input-vaadin-combo-box-14")).click();
        driver.findElement(By.id("vaadin-combo-box-item-0")).click();
        driver.findElement(By.id("input-vaadin-text-field-15")).sendKeys("Lohmar");
        driver.findElement(By.id("textarea-vaadin-text-area-16")).sendKeys("A job description or JD is a written narrative that describes the general tasks, or other related duties, and responsibilities of a position.");

        driver.findElement(By.cssSelector("vaadin-button:nth-child(1)")).click(); // Speichern Button

        //Stellenausschreibung löschen
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Meine Stellenausschreibungen"))).click();
        //wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("vaadin-button:nth-child(7)"))).click(); // Löschen Button
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}