package com.socialcode.webdriver.pages.ads;

import com.socialcode.webdriver.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by anthonyc on 1/19/16.
 */
public class NewAdsModal extends BasePage {
    private static Logger LOG = LoggerFactory.getLogger(NewAdsModal.class);

    WebDriver driver;

    @FindBy(id = "sc-modal-title")
    protected WebElement modalTitle;

    @FindBy(id = "name")
    protected WebElement adClusterNameEdit;

    @FindBy(id = "start_bulk")
    protected WebElement startBulkUploadLink;

    @FindBy(xpath = "//button[text()='Upload and create ads']")
    protected WebElement createAdsSubmitButton;

    public NewAdsModal (WebDriver aDriver) {
        driver = aDriver;

        pageInitElements(driver,this);

        if (!isPageLoaded()) {
            assert false : "This is not New Ads Modal";
        }

        LOG.info("VERIFIED - New Ads Modal is loaded");
    }

    /**
     *  Checks if modal is loaded
     * @return true if modal with correct title displayed; false otherwise
     */
    public boolean isPageLoaded() {
        LOG.debug("Verifying New Ads Modal is loaded");
        if (waitForElementVisible(driver,modalTitle)) {
            return modalTitle.getText().contains("New ads");
        }
        return false;
    }

    /**
     * Sends ad cluster name value to corresponding edit box
     * @param adClusterName
     */
    public void enterAdClusterName(String adClusterName) {
        adClusterNameEdit.sendKeys(adClusterName);
    }

    /**
     * Clicks start bulk upload link in order to select csv file for bulk upload
     * @param aDriver
     */
    public void clickStartBulkUploadLink(WebDriver aDriver) {
        if (waitForElementClickable(aDriver,startBulkUploadLink)) {
            startBulkUploadLink.click();
        }
    }

    /**
     * Clicks 'Upload and create ads' button to start bulk upload processing
     * @param aDriver
     */
    public void submitToBulkAdsUpload(WebDriver aDriver) {
        if (waitForElementClickable(aDriver,createAdsSubmitButton)) {
            createAdsSubmitButton.click();
        }
    }
}
