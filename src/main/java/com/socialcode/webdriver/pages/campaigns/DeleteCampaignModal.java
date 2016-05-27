package com.socialcode.webdriver.pages.campaigns;

import com.socialcode.webdriver.pages.BasePage;
import com.socialcode.webdriver.pages.initiatives.InitiativePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by anthonyc on 1/24/16.
 */
public class DeleteCampaignModal extends BasePage {
    private static Logger LOG = LoggerFactory.getLogger(DeleteCampaignModal.class);

    WebDriver driver;

    @FindBy(className = "modal-content")
    protected WebElement modalContent;

    @FindBy(id = "sc-modal-title")
    protected WebElement modalTitle;

    @FindBy(className = "sc-initiative-grid-delete-confirmation")
    protected WebElement deleteConfirmSection;

    @FindBy(id = "confirm-delete")
    protected WebElement deleteConfirmButton;

    @FindBy(id = "cancel-delete")
    protected WebElement cancelbutton;


    public DeleteCampaignModal(WebDriver aDriver) {
        driver = aDriver;

        pageInitElements(driver,this);

        if (!isPageLoaded()) {
            assert false : "This is not Delete Campaign Modal";
        }

        LOG.info("VERIFIED - Delete Campaign Modal is loaded");
    }

    /**
     * Checks if Delete Campaign Modal is loaded
     * @return true if modal with correct title is loaded; false otherwise
     */
    public boolean isPageLoaded() {
        LOG.debug("Verifying Delete Campaign Modal is loaded");
        if (waitForElementVisible(driver,modalContent)) {
            return modalTitle.getText().contentEquals("Delete campaign?");
        }
        return false;
    }

    /**
     * Clicks the confirm delete button on the modal
     * @param aDriver
     * @return Initiative page which originally holds the deleted campaign is displayed
     */
    public InitiativePage confirmDeletion(WebDriver aDriver) {
        deleteConfirmButton.click();
        return (new InitiativePage(aDriver));
    }
}
