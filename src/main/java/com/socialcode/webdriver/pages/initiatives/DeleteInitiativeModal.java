package com.socialcode.webdriver.pages.initiatives;

import com.socialcode.webdriver.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by anthonyc on 1/23/16.
 */
public class DeleteInitiativeModal extends BasePage {
    private static Logger LOG = LoggerFactory.getLogger(DeleteInitiativeModal.class);

    private WebDriver driver;

    @FindBy(id = "sc-modal-title")
    protected WebElement modalTitle;

    @FindBy(className = "sc-initiative-grid-delete-confirmation")
    protected WebElement modalConfirm;

    @FindBy(id = "initiative-grid-confirm-delete")
    protected WebElement deleteInitButton;

    @FindBy(id = "initiative-grid-cancel-delete")
    protected WebElement deleteInitCancel;

    @FindBy(className = "close")
    protected WebElement closeButton;


    public DeleteInitiativeModal(WebDriver d) {
        driver = d;

        pageInitElements(driver,this);

        if (!isPageLoaded() || !modalTitle.getText().contentEquals("Delete initiative")) {
            assert false : "This is not 'Delete initiative' modal";
        }
        LOG.info("VERIFIED - 'Delete initiative' modal is loaded");
    }

    /**
     * Checks if 'Delete initiative' modal is loaded
     * @return true if modal is loaded; false otherwise
     */
    public boolean isPageLoaded() {
        LOG.debug("Verifying 'Delete initiative' modal is loaded");
        return waitForElementVisible(driver,modalTitle);
    }

    /**
     * Confirms initiative deletion on the modal
     * @param aDriver
     * @return Initiative List Page object if successful; null otherwise
     */
    public InitiativesListPage deleteConfirm(WebDriver aDriver) {
        if (waitForElementVisible(aDriver,deleteInitButton)) {
            deleteInitButton.click();
            if (waitForElementNotVisible(aDriver,120,"//*[@id = 'sc-modal-title']")) {
                waitForPageLoaded(aDriver);
                waitForAjax(aDriver);
                return (new InitiativesListPage(aDriver));
            }
        }
        return null;
    }

}
