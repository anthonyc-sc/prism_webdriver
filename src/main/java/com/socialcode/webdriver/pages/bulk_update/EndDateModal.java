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

    @FindBy(xpath = "//input[@name = 'start_time']")
    protected WebElement startDateEdit;

    @FindBy(xpath = "//input[@name = 'end_time']")
    protected WebElement endDateEdit;

    @FindBy(xpath = "//input[@name = 'time']")
    protected WebElement endTimeEdit;

    public EndDateModal(WebDriver aDriver,String modalTitle) {
        super (aDriver,modalTitle);

        pageInitElements(driver,this);
    }

    public boolean setEndDate(WebDriver aDriver,String endDate) {
        return setDate(aDriver,endDate,endDateEdit);
    }

    public boolean setStartDate(WebDriver aDriver,String startDate) {
        return setDate(aDriver,startDate,startDateEdit);
    }

    public boolean setDate(WebDriver aDriver,String dateValue,WebElement dateEdit) {
        if (waitForElementVisible(aDriver,dateEdit)) {
            type(dateEdit,dateValue);
            if (waitForElementPresence(aDriver,"//td[@class ='active day']")) {
                WebElement element = aDriver.findElement(By.xpath("//td[@class ='active day']"));
                element.click();
            }
            if (dateEdit.getAttribute("value").contentEquals(dateValue)) {
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

    public Boolean updateDateTime(WebDriver aDriver,String startDate,String endDate,String endTime) {
        waitForPageLoaded(aDriver);

        Boolean success = true;
        if (!startDate.isEmpty()) {
            success = success && setStartDate(aDriver,startDate);
        }

        success = success && setEndDate(aDriver,endDate);

        if (!endTime.isEmpty()) {
            success = success && setEndTime(aDriver,endTime);
        }

        if (success) {
            if (waitForElementClickable(aDriver,updateButton)) {
                updateButton.click();
                return true;
            }
        }

        return false;
    }
}
