package com.socialcode.webdriver.pages.bulk_update;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by anthonyc on 1/28/16.
 */
public class BidModal extends BulkUpdateModal {
    private static Logger LOG = LoggerFactory.getLogger(BidModal.class);

    @FindBy(id = "text")
    protected WebElement bidEdit;


    public BidModal(WebDriver aDriver) {
        super(aDriver,"Bid amount");

        pageInitElements(driver,this);
    }

    public boolean bulkBidUpdate(WebDriver aDriver,Double bid) {
        if (waitForElementVisible(aDriver,bidEdit)) {
            type(bidEdit,bid.toString());
            updateButton.click();
            return true;
        }
        return false;
    }
}
