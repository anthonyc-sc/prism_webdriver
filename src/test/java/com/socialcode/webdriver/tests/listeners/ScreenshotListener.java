package com.socialcode.webdriver.tests.listeners;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import com.socialcode.webdriver.tests.WebDriverSetup;

import java.io.File;

/**
 * Created by anthonyc on 2/16/16.
 */
public class ScreenshotListener extends TestListenerAdapter {
    private static Logger LOG = LoggerFactory.getLogger(ScreenshotListener.class);

    @Override
    public void onTestFailure(ITestResult failingTest) {
        try {
            WebDriverSetup t = (WebDriverSetup)failingTest.getInstance();
            WebDriver aDriver = t.getDriver();

            File screenshotFile = ((TakesScreenshot) aDriver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshotFile, new File(System.getProperty("user.dir") + System.getProperty("file.separator") +
            System.currentTimeMillis() + "_" + failingTest.getName() + ".png"));
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Unable to take screenshot for test: " + failingTest.getName());
        }
    }

}
