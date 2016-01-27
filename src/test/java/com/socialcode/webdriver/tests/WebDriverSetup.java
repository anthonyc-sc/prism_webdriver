package com.socialcode.webdriver.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.net.URL;

/**
 * Created by anthonyc on 12/9/15.
 */
public class WebDriverSetup {
    protected RemoteWebDriver driver;

    protected TestData data;

    protected final String prismURL = "https://staging.socialcodedev.com/advisor-v2/";
  //  protected final String prismURL = System.getProperty("environment");

    protected final String loginID = "qateam@socialcode.com";

    protected final String password = "oNievooc";


    /**
     * Instantiates Web Driver object
     */
    @BeforeMethod
    public void setUp() {
        // Currently only handles Chrome browser. Code needs to be added to support other browsers.

        System.setProperty("webdriver.chrome.driver", "/Users/anthonyc/chromedriver/chromedriver");

        try {
            driver = new RemoteWebDriver(new URL("http://localhost:9515"), DesiredCapabilities.chrome());
            driver.manage().window().maximize();
        } catch (Exception e) {

        }
    }

    /**
     * Quits Web Driver object
     */
    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
