package com.socialcode.webdriver.tests;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.net.URL;

/**
 * Created by anthonyc on 12/9/15.
 */
public class WebDriverSetup {
    protected RemoteWebDriver driver;

    protected TestData data;

    //protected final String prismURL = System.getProperty("environment");
    //protected final String loginID = System.getProperty("username");
    //protected final String password = System.getProperty("password");
    //protected final String environment = System.getProperty("env");
    //protected String dataFolder = System.getProperty("file.separator")+ "data" + System.getProperty("file.separator") + environment.toLowerCase() + System.getProperty("file.separator");

    protected final String prismURL = "https://staging.socialcodedev.com/advisor-v2/";
    protected final String loginID="qateam@socialcode.com";
    protected final String password="oNievooc";
    protected final String environment = "qa";
    protected String dataFolder = System.getProperty("file.separator")+ "data" + System.getProperty("file.separator") + environment.toLowerCase() + System.getProperty("file.separator");

    /**
     * Instantiates Web Driver object
     */
    @BeforeMethod
    public synchronized void setUp() {
        // Currently only handles Chrome browser. Code needs to be added to support other browsers.
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.home") + "/chromedriver/chromedriver");

        try {
            driver = new RemoteWebDriver(new URL("http://localhost:9515"), DesiredCapabilities.chrome());
            driver.manage().window().maximize();
        } catch (Exception e) {

        }
    }

    public synchronized WebDriver getDriver() {
        return this.driver;
    }

    /**
     * Quits Web Driver object
     */
    @AfterMethod
    public synchronized void tearDown() {
        driver.quit();
    }
}
