package com.socialcode.webdriver.tests;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anthonyc on 12/9/15.
 */

public class WebDriverSetup {
    protected RemoteWebDriver driver;

    protected TestData data;
    protected final String prismURL = System.getProperty("environment");
    protected final String environment = System.getProperty("env");
    protected String platF = System.getProperty("platform");
    protected String dataFolder = "data" + System.getProperty("file.separator") + environment.toLowerCase() + System.getProperty("file.separator") + platF + System.getProperty("file.separator");
    protected List<String> cookie = null;
    protected String cookieName = "bouncer";
    protected String cookieValue = System.getProperty("cookie");
    protected String cookieExpiration = "Fri Dec 30 14:30:57 PDT 2016";
    protected String isSecure = "False";
    protected String browser  = System.getProperty("browser");
    protected String browserVersion = System.getProperty("version");
    protected String hubURL = System.getProperty("hub");

    /**
     * Instantiates Web Driver object
     */
    @BeforeMethod(groups = {"campaigns","create_campaigns","delete_campaigns","bulk_update","initiative","create_initiative","delete_initiative"})
    public synchronized void setUp() {
        try {
            System.out.println("Inside WebDriver setUp()");
            cookie = new ArrayList<String>();
            cookie.add(cookieName);
            cookie.add("\"" + cookieValue + "\"");
            cookie.add(prismURL.split("/")[2]);
            cookie.add("/");
            cookie.add(cookieExpiration);
            cookie.add(isSecure);

            DesiredCapabilities capability = new DesiredCapabilities();
            capability.setBrowserName(browser);
           // capability.setVersion(browserVersion);
            capability.setCapability(CapabilityType.PROXY,
                    new Proxy().setHttpProxy("localhost:8080"));
            capability.setPlatform(Platform.fromString(System.getProperty("os")));

            System.out.println(Platform.fromString(System.getProperty("os")));

            if (browser.contentEquals("chrome")) {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("start-maximized");
                options.addArguments("allow-running-insecure-content");
                options.addArguments("ignore-certificate-errors");
                capability.setCapability(ChromeOptions.CAPABILITY,options);
            }

            driver = new RemoteWebDriver(new URL(hubURL),capability);
            //   System.setProperty("webdriver.chrome.driver", System.getProperty("user.home") + "/chromedriver/chromedriver");
            //   driver = new RemoteWebDriver(new URL("http://localhost:9515"), capability);
            // driver.manage().window().maximize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized WebDriver getDriver() {
        return this.driver;
    }

    /**
     * Quits Web Driver object
     */
    @AfterMethod(groups = {"campaigns","create_campaigns","delete_campaigns","bulk_update","initiative","create_initiative","delete_initiative"})
    public synchronized void tearDown() {
        System.out.println("Inside WebDriver tearDown()");
        if (driver != null) {
            driver.quit();
        }
    }
}
