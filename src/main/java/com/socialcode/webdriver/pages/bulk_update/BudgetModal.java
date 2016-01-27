package com.socialcode.webdriver.pages.bulk_update;

import com.socialcode.webdriver.pages.facebook.FacebookCampaign;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by anthonyc on 1/27/16.
 */
public class BudgetModal extends BulkUpdateModal {
    private static Logger LOG = LoggerFactory.getLogger(BudgetModal.class);

    @FindBy(id = "text")
    protected WebElement budgetEditBox;

    public BudgetModal(WebDriver aDriver) {
        super(aDriver);

        pageInitElements(driver,this);

        if (!isPageLoaded()) {
            assert false : "This is not Budget Modal";
        }

        LOG.info("VERIFIED - Budget Modal is loaded");
    }

    /**
     * Checks if Status modal is loaded.
     * @return true if modal is loaded;false otherwise
     */
    public boolean isPageLoaded() {
        LOG.debug("Verifying  is Budget Modal loaded");
        waitForPageLoaded(driver);
        if (waitForElementVisible(driver,modalTitle)) {
            return modalTitle.getText().contentEquals("Lifetime budget");
        }
        return false;
    }
}
