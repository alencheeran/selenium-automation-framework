package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.InventoryPage;
import pages.LoginPage;
import utils.ConfigReader;

public class FullFrameworkTest extends BaseTest {

    @Test
    public void validLoginTest() {
        test = extent.createTest("Valid Login Test");
        log.info("Starting valid login test");

        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventoryPage = new InventoryPage(driver);

        loginPage.login(
                ConfigReader.getValidUsername(),
                ConfigReader.getValidPassword()
        );

        Assert.assertTrue(inventoryPage.isLoaded());
        log.info("Valid login test completed successfully");
    }

    @Test
    public void invalidLoginTest() {
        test = extent.createTest("Invalid Login Test");
        log.info("Starting invalid login test");

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login("wrong_user", "wrong_pass");

        String error = loginPage.getErrorMessage();
        Assert.assertTrue(error.contains("do not match"));
        log.info("Invalid login test completed successfully");
    }

    @Test
    public void intentionalFailTest() {
        test = extent.createTest("Intentional Fail Test - Screenshot Demo");
        log.info("Starting intentional fail test");

        // This test deliberately fails to demonstrate screenshot capture
        Assert.assertEquals("actual", "expected", "Deliberate failure to show screenshot");
    }
}