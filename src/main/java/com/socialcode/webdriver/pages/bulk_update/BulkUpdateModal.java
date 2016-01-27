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

    public BulkUpdateModal(WebDriver aDriver) {
        driver = aDriver;
    }

}
