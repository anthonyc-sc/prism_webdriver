package com.socialcode.webdriver.pages.initiatives;

import com.socialcode.webdriver.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by anthonyc on 1/21/16.
 */
public class AddAccountModal extends BasePage {
    private static Logger LOG = LoggerFactory.getLogger(AddAccountModal.class);

    @FindBy(id = "sc-modal-title")
    protected WebElement modalTitle;

    @FindBy(xpath = "//*[@class = 'add-account-view']/div/div")
    protected WebElement platformListBox;

    @FindBy(className = "platform-selection")
    protected WebElement platformSelection;

    @FindBy(xpath = "//*[@for = 'account']/../div")
    protected WebElement accountBox;

    @FindBy(xpath = "//*[@for = 'account']/../div/div/a/span/span")
    protected WebElement accountSelected;

    @FindBy(xpath = "//*[@class = 'account-sections']/div[3]/button[text()='Add account']")
    protected WebElement addAccountButton;

    @FindBy(xpath = "//*[@for = 'asset']/../div")
    protected WebElement assetBox;

    @FindBy(xpath = "//li[text() = 'Please enter 1 or more character']/../../div/input")
    protected WebElement assetSearchEdit;

    @FindBy(xpath = "//*[@for = 'asset']/../div/div/a/span/span")
    protected WebElement assetSelected;

    public AddAccountModal(WebDriver d) {
        driver = d;

        pageInitElements(driver,this);

        if (!isPageLoaded() || !modalTitle.getText().contentEquals("Add account")) {
            assert false : "This is not 'Add Account' modal";
        }
        LOG.info("VERIFIED - 'Add Account' modal is loaded");
    }

    /**
     * Checks if 'Add Account' modal is loaded
     * @return true if modal is loaded; false otherwise
     */
    public boolean isPageLoaded() {
        LOG.debug("Verifying 'Add Account' modal is loaded");
        return waitForElementVisible(driver,modalTitle);
    }

    /**
     * Selects specified platform on the Add Account modal
     * @param aDriver
     * @param pf
     * @return true if successful; false otherwise
     */
    public boolean selectPlatform(WebDriver aDriver,String pf) {
        if (waitForElementClickable(aDriver,platformListBox)) {
            platformListBox.click();
        }

        if (waitForElementClickable(aDriver,aDriver.findElement(By.xpath("//span[text()='"+pf+"']")))) {
            WebElement element = aDriver.findElement(By.xpath("//span[text()='"+pf+"']"));
            element.click();
        }
        if (platformSelection.getText().contentEquals(pf)) {
            return true;
        }
        return false;
    }

    /**
     * Selects specified account on the Add Account Modal
     * @param aDriver
     * @param account
     * @return true if successful; false otherwise
     */
    public boolean selectAccount(WebDriver aDriver, String account) {
        accountBox.click();
        if (waitForElementPresence(aDriver,"//span[text()='"+account+"']")) {
            WebElement element = aDriver.findElement(By.xpath("//span[text()='"+account+"']"));
            element.click();
            return (accountSelected.getText().contentEquals(account));
        }
        return false;
    }

    /**
     * Select specified asset on the Add Account Modal
     * @param aDriver
     * @param asset
     * @return true if successful; false otherwise
     */
    public boolean selectAsset(WebDriver aDriver,String asset) {
        assetBox.click();
        if (waitForElementVisible(aDriver,assetSearchEdit)) {
            assetSearchEdit.sendKeys(asset);
            if (waitForElementPresence(aDriver,"//span[text()='"+asset+"']")) {
                WebElement element = aDriver.findElement(By.xpath("//span[text()='"+asset+"']"));
                element.click();
                return assetSelected.getText().contentEquals(asset);
            }
        }
        return false;
    }

    /**
     * Clicks Submit button on the Add Account Modal
     * @param aDriver
     * @return Initiative Edit Page object if successful; failure otherwise
     */
    public InitiativeEditPage submitAddAccount(WebDriver aDriver) {
        addAccountButton.click();
        return (new InitiativeEditPage(aDriver));
    }


}
