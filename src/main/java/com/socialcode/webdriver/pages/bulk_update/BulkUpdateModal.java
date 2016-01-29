package com.socialcode.webdriver.pages.bulk_update;

import com.socialcode.webdriver.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by anthonyc on 1/26/16.
 */
public class BulkUpdateModal extends BasePage {
    private static Logger LOG = LoggerFactory.getLogger(BulkUpdateModal.class);

    protected WebDriver driver;

    @FindBy(id = "sc-modal-title")
    protected WebElement modalTitle;

    @FindBy(id = "submit")
    protected WebElement updateButton;

    @FindBy(id = "cancel")
    protected WebElement cancelButton;

    public BulkUpdateModal(WebDriver aDriver,String title) {
        driver = aDriver;

        pageInitElements(aDriver,this);

        if (!isPageLoaded(title)) {
            assert false : "This is not " + title +" Modal";
        }

        LOG.info("VERIFIED - " + title + " Modal is loaded");
    }

    /**
     * Checks if modal is loaded.
     * @return true if modal is loaded;false otherwise
     */
    public boolean isPageLoaded(String title) {
        LOG.debug("Verifying  is " + title +" Modal loaded");
        waitForPageLoaded(driver);
        if (waitForElementVisible(driver,modalTitle)) {
            return modalTitle.getText().contentEquals(title);
        }
        return false;
    }


}
