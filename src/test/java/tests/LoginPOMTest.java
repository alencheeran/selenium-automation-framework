package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.InventoryPage;

public class LoginPOMTest {

    WebDriver driver;
    LoginPage loginPage;
    InventoryPage inventoryPage;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com");

        // Initialize page objects
        loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);
    }

    @AfterMethod
    public void teardown() {
        driver.quit();
    }

    @Test
    public void validLoginTest() {
        loginPage.login("standard_user", "secret_sauce");

        Assert.assertTrue(
                inventoryPage.isLoaded(),
                "Inventory page did not load after login"
        );

        System.out.println("Valid login passed - on inventory page");
    }

    @Test
    public void invalidLoginTest() {
        loginPage.login("wrong_user", "wrong_pass");

        Assert.assertEquals(
                loginPage.getErrorMessage(),
                "Epic sadface: Username and password do not match any user in this service"
        );

        System.out.println("Invalid login passed - error message verified");
    }

    @Test
    public void productCountTest() {
        loginPage.login("standard_user", "secret_sauce");

        Assert.assertEquals(
                inventoryPage.getProductCount(),
                6,
                "Product count should be 6"
        );

        System.out.println("Products found: " + inventoryPage.getProductNames());
    }

    @Test
    public void addToCartTest() {
        loginPage.login("standard_user", "secret_sauce");
        inventoryPage.addFirstItemToCart();
        inventoryPage.goToCart();

        Assert.assertTrue(
                driver.getCurrentUrl().contains("/cart"),
                "Should be on cart page"
        );

        System.out.println("Add to cart passed - on cart page");
    }
}