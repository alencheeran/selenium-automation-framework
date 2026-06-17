package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.InventoryPage;

public class DataProviderTest {

    WebDriver driver;
    LoginPage loginPage;
    InventoryPage inventoryPage;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com");
        loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);
    }

    @AfterMethod
    public void teardown() {
        driver.quit();
    }

    // Data provider - returns a 2D array of test data
    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        return new Object[][] {
                // { username, password, expectedResult }
                { "standard_user",         "secret_sauce", "success" },
                { "locked_out_user",       "secret_sauce", "failure" },
                { "wrong_user",            "wrong_pass",   "failure" },
                { "",                      "",             "failure" },
                { "performance_glitch_user","secret_sauce","success" }
        };
    }

    // This test runs 5 times - once for each row in the data provider
    @Test(dataProvider = "loginData")
    public void loginDataDrivenTest(String username, String password, String expected) {
        loginPage.login(username, password);

        if (expected.equals("success")) {
            Assert.assertTrue(
                    inventoryPage.isLoaded(),
                    "Expected successful login for user: " + username
            );
            System.out.println("PASS - Login succeeded for: " + username);
        } else {
            Assert.assertFalse(
                    driver.getCurrentUrl().contains("/inventory"),
                    "Expected failed login for user: " + username
            );
            System.out.println("PASS - Login correctly failed for: " + username);
        }
    }
}
