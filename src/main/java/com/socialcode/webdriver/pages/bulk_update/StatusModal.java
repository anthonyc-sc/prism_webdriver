package com.socialcode.webdriver.pages.bulk_update;

import com.socialcode.webdriver.pages.facebook.FacebookCampaign;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by anthonyc on 1/26/16.
 */
public class StatusModal extends BulkUpdateModal {
    private static Logger LOG = LoggerFactory.getLogger(StatusModal.class);

    @FindBy(xpath = "//*[@class ='sc-bulk-update']/form/div/select")
    protected WebElement statusSelectBox;

    public StatusModal(WebDriver aDriver) {
        super(aDriver,"Status");

        pageInitElements(driver,this);
    }

    /**
     * Select status text from status combo box
     * @param aDriver
     * @param status
     * @return true if successful;false otherwise
     */
    public boolean selectStatusByText(WebDriver aDriver,String status) {
        if (waitForElementVisible(aDriver,statusSelectBox)) {
            try {
                selectByText(statusSelectBox, status);
                return (new Select(statusSelectBox)).getFirstSelectedOption().getText().contentEquals(status);
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    /**
     * This method is used to do bulk status update for all adsets under specified facebook campaign
     * @param aDriver
     * @param cpName
     * @param status
     * @return true if successful;false otherwise
     */
    public Boolean updateStatus(WebDriver aDriver,String cpName,String status) {
        waitForPageLoaded(aDriver);
        waitForAjax(aDriver);

        if (selectStatusByText(aDriver,status)) {
            updateButton.click();
            waitForPageLoaded(aDriver);
            waitForAjax(aDriver);
            return true;
        }
        return false;
    }


}
