package com.socialcode.webdriver.pages.initiatives;

import com.socialcode.webdriver.pages.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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

    @FindBy(xpath = "//button[@type = 'submit'][text() = 'Add account']")
    protected WebElement addAccountButton;

    @FindBy(xpath = "//*[@for = 'asset']/../div")
    protected WebElement assetBox;

    @FindBy(xpath = "//li[text() = 'Please enter 1 or more character']/../../div/input")
    protected WebElement assetSearchEdit;

    @FindBy(xpath = "//*[@for = 'asset']/../div/div/a/span/span")
    protected WebElement assetSelected;

    @FindBy(xpath = "//*[@class = 'account-sections']/div/div/form/div[2]/div/div/div/div/a/span")
    protected WebElement instagramAssetBox;

    @FindBy(xpath = "//*[@class = 'account-sections']/div[3]/div/form/div[2]/div/div/div/div/a/span")
    protected WebElement fbAssetBox;

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
        String platform = "";
        Integer nTries = 0;
        do {
            waitForPageLoaded(aDriver);
            waitForAjax(aDriver);
            if (waitForElementClickable(aDriver,platformListBox)) {
                try {
                    platformListBox.click();
                } catch (Exception cE) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException inTEx) {
                    }
                    try {
                        platformListBox.click();
                    } catch (Exception exC) {
                        LOG.error("Unable to click platform list box");
                        return false;
                    }
                }
                waitForAjax(aDriver);
                waitForPageLoaded(aDriver);
                if (waitForElementPresence(aDriver, "//*[@class = 'platform-selection']/span[text()='" + pf + "']") &&
                        waitForElementClickable(aDriver, aDriver.findElement(By.xpath("//*[@class = 'platform-selection']/span[text()='" + pf + "']")))) {
                    try {
                        WebElement element = aDriver.findElement(By.xpath("//*[@class = 'platform-selection']/span[text()='" + pf + "']"));
                        element.click();
                        waitForPageLoaded(aDriver);
                        waitForAjax(aDriver);
                        platform = platformSelection.getText();
                    } catch (NoSuchElementException exN) {
                        exN.printStackTrace();
                    } catch (StaleElementReferenceException exS) {
                        // Retry
                        try {
                            Thread.sleep(2000);
                            if (platformSelection.getText().contentEquals(pf)) {
                                return true;
                            }
                        } catch (StaleElementReferenceException staleEx) {
                            staleEx.printStackTrace();
                            LOG.error("StaleElementReferenceException when selecting platform: " + pf);
                            return false;
                        } catch (InterruptedException intEx) {

                        }
                    } catch (WebDriverException wE) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException iE) {

                        }
                        try {
                            WebElement element = aDriver.findElement(By.xpath("//*[@class = 'platform-selection']/span[text()='" + pf + "']"));
                            element.click();
                            waitForPageLoaded(aDriver);
                            waitForAjax(aDriver);
                            platform = platformSelection.getText();
                        } catch (Exception Excp) {
                            return false;
                        }
                    }

                }
            }
            nTries++;
        } while (!platform.contentEquals(pf) && (nTries < 3));

        return platform.contentEquals(pf);
    }

    /**
     * Selects specified account on the Add Account Modal
     * @param aDriver
     * @param account
     * @return true if successful; false otherwise
     */
    public boolean selectAccount(WebDriver aDriver, String account) {
        if (waitForElementVisible(aDriver,accountBox) && waitForElementClickable(aDriver,accountBox)) {
            accountBox.click();
            if (waitForElementPresence(aDriver,"//span[text()='"+account+"']")) {
                WebElement element = aDriver.findElement(By.xpath("//span[text()='"+account+"']"));
                if (waitForElementClickable(aDriver,element)) {
                    try {
                        element.click();
                        return (accountSelected.getText().contentEquals(account));
                    } catch (WebDriverException e) {
                    }
                }
            }
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
        if (waitForElementVisible(aDriver,assetBox) && waitForElementClickable(aDriver,assetBox)) {
            assetBox.click();
            if (waitForElementVisible(aDriver,assetSearchEdit)) {
                assetSearchEdit.sendKeys(asset);
                waitForAjax(aDriver);
                if (waitForElementPresence(aDriver,"//span[text()='"+asset+"']")) {
                    try {
                        WebElement element = aDriver.findElement(By.xpath("//span[text()='"+asset+"']"));
                        element.click();
                        return assetSelected.getText().contentEquals(asset);
                    } catch (NoSuchElementException nsEx) {
                        // Retry
                        try {
                            Thread.sleep(2000);
                            WebElement element = aDriver.findElement(By.xpath("//span[text()='"+asset+"']"));
                            element.click();
                            return assetSelected.getText().contentEquals(asset);
                        } catch(InterruptedException intEx) {

                        } catch (NoSuchElementException nsExc) {
                            LOG.error("Unable to find selected asset: " + asset);
                        }
                    }
                }
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
        if (waitForElementClickable(aDriver,addAccountButton)) {
            try {
                addAccountButton.click();
                return (new InitiativeEditPage(aDriver));
            } catch (Exception e) {
               e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * This method used to select Instagram Asset on the Add Account Modal
     * @param aDriver
     * @param asset
     * @return true if successful; false otherwise
     */
    public Boolean selectInstagramAsset(WebDriver aDriver,String asset) {
        if (waitForElementVisible(aDriver,instagramAssetBox) && waitForElementClickable(aDriver,instagramAssetBox)) {
            instagramAssetBox.click();
            if (waitForElementPresence(aDriver,"//li[text() = 'Please enter 1 or more character']/../../div/input")) {
                WebElement element = aDriver.findElement(By.xpath("//li[text() = 'Please enter 1 or more character']/../../div/input"));
                element.sendKeys(asset);
                if (waitForElementPresence(aDriver,"//span[text() = '"+asset+"']")) {
                    WebElement item = aDriver.findElement(By.xpath("//span[text() = '"+asset+"']"));
                    item.click();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method used to select FB Asset on the Add Account Modal
     * @param aDriver
     * @param fbAsset
     * @return true if successful; false otherwise
     */
    public Boolean selectFBAssetForInstagram(WebDriver aDriver,String fbAsset) {
        if (waitForElementVisible(aDriver,fbAssetBox) && waitForElementClickable(aDriver,fbAssetBox)) {
            fbAssetBox.click();
            if (waitForElementPresence(aDriver,"//li[text() = 'Please enter 1 or more character']/../../div/input")) {
                WebElement element = aDriver.findElement(By.xpath("//li[text() = 'Please enter 1 or more character']/../../div/input"));
                element.sendKeys(fbAsset);
                if (waitForElementPresence(aDriver,"//span[text() = '"+fbAsset+"']")) {
                    WebElement item = aDriver.findElement(By.xpath("//span[text() = '"+fbAsset+"']"));
                    item.click();
                    return true;
                }
            }
        }
        return false;
    }
}
