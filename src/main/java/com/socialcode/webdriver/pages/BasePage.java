package com.socialcode.webdriver.pages;

import static java.util.concurrent.TimeUnit.SECONDS;

import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anthonyc on 12/1/15.
 */
public class BasePage {
    private static final Logger LOG = LoggerFactory.getLogger(BasePage.class);

    public static final int PAGE_LOADING_WAIT = 2000;

    protected WebDriver driver;

    public BasePage() {

    }

    /**
     * Set value to web element key
     * @param key
     * @param value
     * @return value set if web element key is not null;otherwise, error message
     */
    protected String type(WebElement key,String value) {
        if (key != null) {
            key.click();
            key.clear();
            key.sendKeys(value);
            // Retry if value displayed not match with expected
            if (!key.getAttribute("value").contentEquals(value)) {
                key.clear();
                key.sendKeys(value);
            }
            return key.getAttribute("value");
        }
        return "WebElement " + key + " is null";
    }


    /**
     * select given text for combo box element
     * @param key
     * @param text
     * @return selected value or error message
     * @throws Exception
     */
    public String selectByText(WebElement key,String text) throws Exception {
        Select comboB = new Select(key);

        if (comboB != null) {
                comboB.selectByVisibleText(text);
                return comboB.getFirstSelectedOption().getText();
        }
        return "Unable to select value for Web Element " + key;
    }

    /**
     * select given value for combo box element
     * @param key
     * @param value
     * @return selected value or error message
     */
    public String selectByValue(WebElement key,String value) {
        Select comboB = new Select(key);

        if (comboB != null) {
            comboB.selectByValue(value);
            return comboB.getFirstSelectedOption().getText();
        }
        return "Unable to select value for Web Element " + key;
    }

    /**
     * Retrieve list of values from the combo box web element
     * @param key
     * @return list of combo box values or empty list
     */
    public List<String> retrieveCBoxValueList(WebElement key) {
        List<String> valueList = new ArrayList<>();

        Select comboB = new Select(key);
        if (comboB != null) {
            List<WebElement> options = comboB.getOptions();
            for (WebElement element: options) {
                valueList.add(element.getText());
            }
        }
        return valueList;
    }

    /**
     * Check if given web element is visible
     * @param key
     * @return true if visible;false otherwise
     */
    public boolean isVisible(final WebElement key) {
        try {
            return key.isDisplayed();
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Wait for page to complete loading; then initialize its elements
     * @param aDriver
     * @param expectedPage
     * @return object page
     */
    public <T> T pageInitElements(WebDriver aDriver,Class<T> expectedPage) {
        waitForPageLoaded(aDriver);
        return PageFactory.initElements(aDriver,expectedPage);
    }

    /**
     * Wait for page to complete loading; then initialize its elements
     * @param aDriver
     * @param expectedPage
     */
    public void pageInitElements(WebDriver aDriver,Object expectedPage) {
        waitForPageLoaded(aDriver);
        PageFactory.initElements(aDriver,expectedPage);
    }

    /**
     * Web Driver fluent wait for page to complete loading within specific page loading wait time
     * @param aDriver
     */
    public void waitForPageLoaded(WebDriver aDriver){
        Wait<WebDriver> wait = new FluentWait<WebDriver>(aDriver).withTimeout(
                PAGE_LOADING_WAIT, SECONDS).pollingEvery(1, SECONDS).ignoring(Exception.class);

        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver aDriver) {
                return ((JavascriptExecutor)aDriver).executeScript("return document.readyState").equals("complete");
            }
        };
        try {
            wait.until(expectation);
        } catch (Throwable error) {
            LOG.error("Timeout waiting for Page Load Request to complete.");
        }
    }

    /**
     * Wrapper to wait for element to be visible
     * @param aDriver
     * @param element
     * @return true if element is visible within specific wait time; false otherwise
     */
    public boolean waitForElementVisible(final WebDriver aDriver, final WebElement element){
        return waitForElementVisible(aDriver,PAGE_LOADING_WAIT,element);
    }

    /**
     * Web Driver fluent wait for element to be visible within specific wait time
     * @param aDriver
     * @param timeout
     * @param element
     * @return true if element is visible; false otherwise
     */
    public boolean waitForElementVisible(final WebDriver aDriver, final int timeout, final WebElement element){
        Wait<WebDriver> wait = new FluentWait<WebDriver>(aDriver).withTimeout(
                timeout, SECONDS).pollingEvery(1, SECONDS).ignoring(Exception.class);
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Throwable error) {
            LOG.error("Timeout waiting for element to visible.");
        }
        return isVisible(element);
    }

    /**
     * Wrapper for wait for element to be present within specific wait time
     * @param aDriver
     * @param elementPath
     * @return true if element is present; false otherwise
     */
    public boolean waitForElementPresence(final WebDriver aDriver,String elementPath) {
        return waitForElementPresence(aDriver,PAGE_LOADING_WAIT,elementPath);
    }

    /**
     * Web Driver fluent wait for element to be present within specific wait time
     * @param aDriver
     * @param timeout
     * @param elementPath
     * @return true if element is present;false otherwise
     */
    public boolean waitForElementPresence(final WebDriver aDriver, final int timeout, String elementPath){
        Wait<WebDriver> wait = new FluentWait<WebDriver>(aDriver).withTimeout(
                timeout, SECONDS).pollingEvery(1, SECONDS).ignoring(Exception.class);
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(elementPath)));
        } catch (Throwable error) {
            LOG.error("Timeout waiting for element to be presence.");
        }
        return isVisible(aDriver.findElement(By.xpath(elementPath)));
    }


    /**
     * Wrapper for wait for element to be clickable within specific wait time
     * @param aDriver
     * @param element
     * @return true if element is clickable; false otherwise
     */
    public boolean waitForElementClickable(final WebDriver aDriver,final WebElement element) {
        return waitForElementClickable(aDriver,PAGE_LOADING_WAIT,element);
    }

    /**
     * Web Driver fluent wait for element to be clickable within specific wait time
     * @param aDriver
     * @param timeout
     * @param element
     * @return true if element is clickable; false otherwise
     */
    public boolean waitForElementClickable(final WebDriver aDriver, final int timeout, final WebElement element){
        WebElement  foundElement = null;
        Wait<WebDriver> wait = new FluentWait<WebDriver>(aDriver).withTimeout(
                timeout, SECONDS).pollingEvery(1, SECONDS).ignoring(Exception.class);
        try {
            foundElement = wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (Throwable error) {
            LOG.error("Timeout waiting for element to be clickable.");
        }
        return (foundElement != null);
    }

    /**
     * Wait for Async job to complete processing
     * @param aDriver
     */
    public void waitForAjax(final WebDriver aDriver)
    {
        while (true)
        {
            Boolean ajaxIsComplete = (Boolean) ((JavascriptExecutor)aDriver).executeScript("return jQuery.active == 0");
            if (ajaxIsComplete){
                break;
            }
            try {
                Thread.sleep(100);
            } catch (Exception e) {

            }
        }
    }
}
