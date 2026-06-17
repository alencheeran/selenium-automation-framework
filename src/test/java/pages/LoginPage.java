package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {

    // 1. Driver reference
    WebDriver driver;
    WebDriverWait wait;

    // 2. Locators - all in one place
    By usernameField = By.id("user-name");
    By passwordField = By.id("password");
    By loginButton = By.id("login-button");
    By errorMessage = By.cssSelector("[data-test='error']");

    // 3. Constructor - receives driver from test class
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // 4. Actions - methods that describe what a user does on this page
    public void enterUsername(String username) {
        driver.findElement(usernameField).sendKeys(username);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(loginButton).click();
    }

    // Combines all login steps into one method
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    public String getErrorMessage() {
        WebElement error = wait.until(
                ExpectedConditions.visibilityOfElementLocated(errorMessage)
        );
        return error.getText();
    }
}
