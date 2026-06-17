package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import utils.ConfigReader;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseTest {

    protected WebDriver driver;
    protected static ExtentReports extent;
    protected ExtentTest test;
    protected static final Logger log = LogManager.getLogger(BaseTest.class);

    // Runs ONCE before entire test suite
    @BeforeSuite
    public void setupReport() {
        ExtentSparkReporter spark = new ExtentSparkReporter(
                System.getProperty("user.dir") + "/reports/TestReport.html"
        );
        spark.config().setDocumentTitle("Automation Test Report");
        spark.config().setReportName("SauceDemo Test Suite");

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("Browser", ConfigReader.getBrowser());

        log.info("Extent Report initialized");
    }

    // Runs ONCE after entire test suite
    @AfterSuite
    public void teardownReport() {
        extent.flush(); // writes report to file
        log.info("Extent Report saved");
    }

    // Runs before EVERY test method
    @BeforeMethod
    public void setup() {
        ChromeOptions options = new ChromeOptions();

        // Check if headless mode is requested (CI/CD environments)
        boolean isHeadless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        if (isHeadless) {
            options.addArguments("--headless");
        }

        // Required for GitHub Actions and other Linux environments
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");

        // Additional stability improvements
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-plugins");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get(ConfigReader.getBaseUrl());
        log.info("Browser opened and navigated to: " + ConfigReader.getBaseUrl());
    }

    // Runs after EVERY test method
    @AfterMethod
    public void teardown(ITestResult result) {
        // Screenshot on failure
        if (result.getStatus() == ITestResult.FAILURE) {
            log.error("Test FAILED: " + result.getName());
            String screenshotPath = takeScreenshot(result.getName());
            try {
                test.addScreenCaptureFromPath(screenshotPath);
            } catch (Exception e) {
                log.error("Could not attach screenshot: " + e.getMessage());
            }
            test.log(Status.FAIL, result.getThrowable());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            log.info("Test PASSED: " + result.getName());
            test.log(Status.PASS, "Test passed");
        }

        driver.quit();
        log.info("Browser closed");
    }

    // Takes screenshot and saves to screenshots folder
    private String takeScreenshot(String testName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String screenshotPath = System.getProperty("user.dir")
                + "/screenshots/" + testName + "_" + timestamp + ".png";
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(src, new File(screenshotPath));
            log.info("Screenshot saved: " + screenshotPath);
        } catch (IOException e) {
            log.error("Screenshot failed: " + e.getMessage());
        }
        return screenshotPath;
    }
}
