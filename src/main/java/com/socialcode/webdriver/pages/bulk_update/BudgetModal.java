package com.socialcode.webdriver.pages.bulk_update;

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

    public BudgetModal(WebDriver aDriver,String title) {
        super(aDriver,title);

        pageInitElements(driver,this);
    }

    /**
     * Set budget edit box with given input value on the Budget Modal
     * @param aDriver
     * @param budget
     * @return true if successful;false otherwise
     */
    public boolean bulkBudgetUpdate(WebDriver aDriver,Double budget) {
        if (waitForElementVisible(aDriver,budgetEditBox)) {
            type(budgetEditBox,budget.toString());
            updateButton.click();
            return true;
        }
        return false;
    }

}
