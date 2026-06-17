import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import org.openqa.selenium.Alert;

public class FirstTest {
        public static void main(String[] args) {
            WebDriver driver = new ChromeDriver();
            driver.manage().window().maximize();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            driver.get("https://demoqa.com/alerts");

            // Click button that triggers a simple alert
            driver.findElement(By.id("alertButton")).click();

            // Switch to alert, read text, accept it
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            System.out.println("Alert text: " + alert.getText());
            alert.accept(); // clicks OK

            // Confirm box - accept or dismiss
            driver.findElement(By.id("confirmButton")).click();
            Alert confirm = wait.until(ExpectedConditions.alertIsPresent());
            System.out.println("Confirm text: " + confirm.getText());
            confirm.dismiss(); // clicks Cancel
            System.out.println("Result: " +
                    driver.findElement(By.id("confirmResult")).getText());

            driver.quit();
        }
    }