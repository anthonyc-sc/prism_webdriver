package com.socialcode.webdriver.pages.login;

import com.socialcode.webdriver.pages.BasePage;
import com.socialcode.webdriver.pages.initiatives.InitiativesListPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by anthonyc on 12/1/15.
 */
public class LoginPage extends BasePage {
    private static Logger LOG = LoggerFactory.getLogger(LoginPage.class);

    @FindBy(id = "username")
    protected WebElement scLogin = null;

    @FindBy(id = "password")
    protected WebElement scPassword = null;

    @FindBy(xpath = "//button[text()='Sign in with SocialCode']")
    protected WebElement submitButton = null;

    @FindBy(id = "auth-container")
    protected WebElement authPanel;

    public LoginPage(WebDriver d) {
        driver = d;

        pageInitElements(driver,this);

        if (!isPageLoaded(d)) {
            assert false : "This is not 'Login' page";
        }
        LOG.info("VERIFIED - 'Login' page is loaded");
    }

    /**
     * Check if current page is loaded
     * @return true if loaded; false otherwise
     */
    public boolean isPageLoaded(WebDriver aDriver) {
        LOG.debug("Verifying 'Login' page is loaded");
        waitForPageLoaded(aDriver);
        return waitForElementVisible(aDriver,authPanel);
    }

    /**
     * Enters given user id to user name edit box of the login page
     * @param id
     * @return current page object
     */
    public LoginPage enterLoginId(String id) {
        scLogin.sendKeys(id);
        return this;
    }

    /**
     * Enters given password to password field of the login page
     * @param password
     * @return  current page object
     */
    public LoginPage enterPassword(String password) {
        scPassword.sendKeys(password);
        return this;
    }

    /**
     * Clicks submit button of the login page
     * @return Initiative List Page object
     * @param aDriver
     */
    public InitiativesListPage submit(WebDriver aDriver) {
        submitButton.click();
        waitForPageLoaded(aDriver);
        return (new InitiativesListPage(aDriver));
    }
}
