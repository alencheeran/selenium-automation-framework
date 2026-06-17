package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class InventoryPage {

    WebDriver driver;
    WebDriverWait wait;

    By productNames = By.className("inventory_item_name");
    By cartIcon = By.className("shopping_cart_link");
    By addToCartButton = By.cssSelector(".btn_inventory");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isLoaded() {
        wait.until(ExpectedConditions.urlContains("/inventory"));
        return driver.getCurrentUrl().contains("/inventory");
    }

    public int getProductCount() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(productNames));
        return driver.findElements(productNames).size();
    }

    public List<String> getProductNames() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(productNames));
        List<WebElement> elements = driver.findElements(productNames);
        List<String> names = new java.util.ArrayList<>();
        for (WebElement e : elements) {
            names.add(e.getText());
        }
        return names;
    }

    public void addFirstItemToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
    }

    public void goToCart() {
        driver.findElement(cartIcon).click();
    }
}