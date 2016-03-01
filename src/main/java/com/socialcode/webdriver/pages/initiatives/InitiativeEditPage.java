package com.socialcode.webdriver.pages.initiatives;

import com.socialcode.webdriver.pages.BasePage;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by anthonyc on 12/3/15.
 */
public class InitiativeEditPage extends BasePage {
    private static Logger LOG = LoggerFactory.getLogger(InitiativeEditPage.class);

    WebDriver driver;

    @FindBy(className = "toolbar-second-name")
    protected WebElement toolbarName = null;

    @FindBy(className = "account-assets-header")
    protected WebElement accountAssetsHeader = null;

    @FindBy(id = "save-initiative")
    protected WebElement saveButton = null;

    @FindBy(xpath = "//*[@class = 'account-assets-header']/button")
    protected WebElement addAccountBtn = null;


    public InitiativeEditPage(WebDriver d,String initName) {
        driver = d;

        pageInitElements(driver,this);

        try {
            if (!isPageLoaded()
                    || (!toolbarName.getText().contentEquals(initName))
                    || (!isVisible(accountAssetsHeader))) {
                assert false : "This is not 'Initiative Edit' page";
            }
        } catch (StaleElementReferenceException e) {
            // Refresh the page. Make sure the current url is the same. Wait for page and any ajax calls to complete,then rechecks for correct page loaded
            String url = driver.getCurrentUrl();
            driver.navigate().refresh();
            if (!driver.getCurrentUrl().contentEquals(url)) {
                driver.get(url);
            }
            waitForPageLoaded(driver);
            waitForAjax(driver);

            pageInitElements(driver,this);

            if (!isPageLoaded()
                    || (!toolbarName.getText().contentEquals(initName))
                    || (!isVisible(accountAssetsHeader))) {
                assert false : "This is not 'Initiative Edit' page";
            }
        }

        LOG.info("VERIFIED - 'Initiative Edit' page is loaded");
    }

    public InitiativeEditPage (WebDriver d) {
        driver = d;

        pageInitElements(driver,this);

        if (!isPageLoaded()
                    || (!isVisible(accountAssetsHeader))) {
                assert false : "This is not 'Initiative Edit' page";
        }
        LOG.info("VERIFIED - 'Initiative Edit' page is loaded");
    }

    /**
     * Checks if 'Initiative Edit' page is loaded
     * @return true if page is loaded; false otherwise
     */
    public boolean isPageLoaded() {
        LOG.debug("Verifying 'Initiative Edit' page is loaded");
        waitForPageLoaded(driver);
        return waitForElementVisible(driver,toolbarName);
    }

    /**
     * Save changes on the Initiative Edit page
     * @return Initiative Page object if successful; null otherwise
     * @throws Exception
     */
    public InitiativePage saveChanges() throws Exception {
        if (isVisible(saveButton) && waitForElementClickable(driver,saveButton)) {
            try {
                saveButton.click();
                return (new InitiativePage(driver));
            } catch (WebDriverException e) {
                // Wait some time and try again
                Thread.sleep(5000);
                if (waitForElementClickable(driver,saveButton)) {
                    saveButton.click();
                    return (new InitiativePage(driver));
                }
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Clicks Add Account link to launch the modal for adding platform accounts
     * @param aDriver
     * @return Add Account Modal object if successful; failure otherwise
     */
    public AddAccountModal launchAddAccountModal(WebDriver aDriver) {
        waitForPageLoaded(aDriver);
        waitForAjax(aDriver);
        if (waitForElementClickable(aDriver,addAccountBtn)) {
            try {
                Thread.sleep(2000);
                addAccountBtn.click();
                return (new AddAccountModal(aDriver));
            } catch(InterruptedException intExcpt) {

            } catch(Exception e) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException intE) {

                }
                if (waitForElementClickable(aDriver,addAccountBtn)) {
                    addAccountBtn.click();
                    return (new AddAccountModal(aDriver));
                }
            }
        }
        return null;
    }

}
