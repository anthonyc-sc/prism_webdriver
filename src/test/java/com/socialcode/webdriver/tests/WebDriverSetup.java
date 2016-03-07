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

    //protected final String prismURL = "https://beta.socialcode.com/advisor/#initiative";
    protected final String prismURL = System.getProperty("environment");

    protected final String environment = System.getProperty("env");
    //protected final String environment = "prod";
    protected String platF = System.getProperty("platform");
    //protected final String platF = "all";
    protected String dataFolder = System.getProperty("file.separator")+ "data" + System.getProperty("file.separator") + environment.toLowerCase() + System.getProperty("file.separator") + platF + System.getProperty("file.separator");
    protected List<String> cookie = null;
    protected String cookieName = "bouncer";
    protected String cookieValue = System.getProperty("cookie");
    protected String cookieExpiration = "Fri Mar 18 14:30:57 PDT 2016";
    protected String isSecure = "False";

    //protected final String loginID="qateam@socialcode.com";
    //protected final String password="oNievooc";
    //protected final String loginID = System.getProperty("username");
    //protected final String password = System.getProperty("password");

    /**
     * Instantiates Web Driver object
     */
    @BeforeMethod(groups = {"campaigns","create_campaigns","delete_campaigns","bulk_update","initiative","create_initiative","delete_initiative"})
    public synchronized void setUp() {
        // Currently only handles Chrome browser. Code needs to be added to support other browsers.
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.home") + "/chromedriver/chromedriver");

        try {
            cookie = new ArrayList<String>();
            cookie.add(cookieName);
            cookie.add("\"" + cookieValue + "\"");
            cookie.add(prismURL.split("/")[2]);
            cookie.add("/");
            cookie.add(cookieExpiration);
            cookie.add(isSecure);

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
    @AfterMethod(groups = {"campaigns","create_campaigns","delete_campaigns","bulk_update","initiative","create_initiative","delete_initiative"})
    public synchronized void tearDown() {
        driver.quit();
    }
}
