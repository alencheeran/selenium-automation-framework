
package tests;

import com.opencsv.CSVReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.InventoryPage;
import pages.LoginPage;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CSVDataDrivenTest {

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

    // Reads CSV and returns data as 2D array - same structure as @DataProvider
    @DataProvider(name = "csvLoginData")
    public Object[][] getCSVData() throws Exception {
        List<Object[]> data = new ArrayList<>();

        CSVReader reader = new CSVReader(new FileReader("loginData.csv"));
        String[] line;
        boolean isHeader = true;

        while ((line = reader.readNext()) != null) {
            if (isHeader) {
                isHeader = false; // skip header row
                continue;
            }
            data.add(new Object[]{ line[0], line[1], line[2] });
        }
        reader.close();

        return data.toArray(new Object[0][]);
    }

    @Test(dataProvider = "csvLoginData")
    public void csvLoginTest(String username, String password, String expected) {
        loginPage.login(username, password);

        if (expected.equals("success")) {
            Assert.assertTrue(
                    inventoryPage.isLoaded(),
                    "Expected success for: " + username
            );
            System.out.println("PASS - Login succeeded for: " + username);
        } else {
            Assert.assertFalse(
                    driver.getCurrentUrl().contains("/inventory"),
                    "Expected failure for: " + username
            );
            System.out.println("PASS - Login correctly failed for: " + username);
        }
    }
}