package com.socialcode.webdriver.pages.bulk_update;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by anthonyc on 1/30/16.
 */
public class EndDateModal extends BulkUpdateModal {
    private static Logger LOG = LoggerFactory.getLogger(EndDateModal.class);

    @FindBy(xpath = "//input[@data-provide = 'datepicker']")
    protected WebElement endDateEdit;

    @FindBy(xpath = "//input[@data-provide = 'timepicker']")
    protected WebElement endTimeEdit;

    public EndDateModal(WebDriver aDriver) {
        super (aDriver,"End Date");

        pageInitElements(driver,this);
    }

    public boolean setEndDate(WebDriver aDriver,String endDate) {
        if (waitForElementVisible(aDriver,endDateEdit)) {
            type(endDateEdit,endDate);
            if (waitForElementPresence(aDriver,"//td[@class ='active day']")) {
                WebElement element = aDriver.findElement(By.xpath("//td[@class ='active day']"));
                element.click();
            }
            if (endDateEdit.getAttribute("value").contentEquals(endDate)) {
                return true;
            }
        }
        return false;
    }

    public boolean setEndTime(WebDriver aDriver,String endTime) {
        if (waitForElementVisible(aDriver,endTimeEdit)) {
            type(endTimeEdit,endTime);
            if (waitForElementPresence(aDriver,"//li[@class = 'ui-timepicker-am ui-timepicker-selected']")) {
                WebElement element = aDriver.findElement(By.xpath("//li[@class = 'ui-timepicker-am ui-timepicker-selected']"));
                if (element.getText().contentEquals(endTime)) {
                    element.click();
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean updateEndDateTime(WebDriver aDriver,String endDate,String endTime) {
        waitForPageLoaded(aDriver);
        if (setEndDate(aDriver,endDate) && setEndTime(aDriver,endTime)) {
            updateButton.click();
            return true;
        }
        return false;
    }
}
