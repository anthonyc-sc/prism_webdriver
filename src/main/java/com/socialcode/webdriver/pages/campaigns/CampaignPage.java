package com.socialcode.webdriver.pages.campaigns;

import com.socialcode.webdriver.pages.BasePage;
import com.socialcode.webdriver.pages.bulk_update.BudgetModal;
import com.socialcode.webdriver.pages.bulk_update.EndDateModal;
import com.socialcode.webdriver.pages.bulk_update.StatusModal;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by anthonyc on 1/7/16.
 */
public class CampaignPage extends BasePage {
    private static Logger LOG = LoggerFactory.getLogger(CampaignPage.class);

    protected WebDriver driver;

    @FindBy(xpath = "//*[@class = 'sc-card-details']/div/h2")
    protected WebElement campaignNameHeader;

    @FindBy(xpath = "//*[@id = 'header-form']/div/a")
    protected WebElement newButton;

    @FindBy(xpath = "//a[text() = 'Ads']")
    protected WebElement newAdds;

    @FindBy(xpath = "//*[@id = 'campaign-details']/div/button")
    protected WebElement dropDownButton;

    @FindBy(id = "delete-campaign")
    protected WebElement deleteCampaignLink;

    @FindBy(xpath = "//*[@id = 'alert-region']/div/div/div")
    protected WebElement alertMessages;

    @FindBy(xpath = "//*[@class ='toast-buttons']/a[2]")
    protected WebElement closeAlertLink;

    @FindBy(xpath = "//*[@class = 'select-all-header-cell']/div/button")
    protected WebElement selectAllCheckBox;

    @FindBy(xpath = "//*[@class = 'select-all-header-cell']/div/button[2]")
    protected WebElement ActionsDropdown;

    public CampaignPage(WebDriver aDriver,String scName) {
        driver = aDriver;

        pageInitElements(driver,this);

        if (!isPageLoaded(scName)) {
            assert false : "This is not Campaign Page";
        }

        LOG.info("VERIFIED - Campaign Page is loaded");
    }

    /**
     * Checks if Campaign Page is loaded
     * @param scName
     * @return true if page is loaded with correct campaign name as header; false otherwise
     */
    public boolean isPageLoaded(String scName) {
        LOG.debug("Verifying Campaign Page is loaded");
        waitForPageLoaded(driver);
        waitForAjax(driver);
        if (waitForElementVisible(driver,campaignNameHeader)) {
            String str = campaignNameHeader.getText();
            System.out.println(str);
            return campaignNameHeader.getText().contentEquals(scName);
        }
        return false;
    }

    /**
     * Clicks New > Ads link to bring up the 'New Ads' modal
     * @param aDriver
     * @throws Exception
     */
    public void launchCreateNewAdsWindow(WebDriver aDriver) throws Exception {
        if (isVisible(newButton)) {
            newButton.click();
        }
        if (waitForElementClickable(aDriver,newAdds)) {
            Thread.sleep(5000);
            newAdds.click();
        }
    }

    /**
     * Clicks drop down > Delete Campaign link on the Campaign Page
     * @param aDriver
     * @return
     */
    public DeleteCampaignModal deleteCampaign(WebDriver aDriver) {
        if (waitForElementVisible(aDriver,dropDownButton)) {
            dropDownButton.click();
            if (waitForElementVisible(aDriver,deleteCampaignLink)) {
                deleteCampaignLink.click();
                return (new DeleteCampaignModal(aDriver));
            }
        }
        return null;
    }

    /**
     * Retrieves alert message
     * @param aDriver
     * @return string containing alert message if it's visible;empty string otherwise
     */
    public String getAlertMessage(WebDriver aDriver) {
        if (waitForElementVisible(aDriver,alertMessages)) {
            return alertMessages.getText();
        }
        return "";
    }

    /**
     * Closes the alert box
     * @param aDriver
     * @return true if successful;false otherwise
     */
    public Boolean closeAlert(WebDriver aDriver) {
        if (waitForElementVisible(aDriver,closeAlertLink)) {
            if (waitForElementClickable(aDriver,closeAlertLink)) {
                try {
                    closeAlertLink.click();
                    return true;
                } catch (WebDriverException e) {
                    // retry
                    try {
                        Thread.sleep(2000);
                        closeAlertLink.click();
                        return true;
                    } catch (WebDriverException ex) {
                        return false;
                    } catch (Exception ex2) {

                    }
                }
            }
        }
        return false;
    }

    /**
     * This method used to select all ad sets row, then click on the Actions header to get the drop down list of actions
     * @param aDriver
     * @return true if successful;false otherwise
     */
    public boolean getActionsDropDown(WebDriver aDriver) {
        if (waitForElementVisible(aDriver,selectAllCheckBox)) {
            selectAllCheckBox.click();
            if (waitForElementVisible(aDriver,ActionsDropdown)) {
                ActionsDropdown.click();
                return true;
            }
        }
        return false;
    }


    /**
     *  This method is used to select all adsets, then open the bulk status update modal
     * @param aDriver
     * @return Status Modal object if successful;null otherwise
     */
    public StatusModal launchBulkStatusUpdateModal(WebDriver aDriver,WebElement setStatusLink) {
        if (getActionsDropDown(aDriver)) {
            if (waitForElementVisible(aDriver,setStatusLink)) {
                setStatusLink.click();
                waitForPageLoaded(aDriver);
                return (new StatusModal(aDriver));
            }
        }
        return null;
    }

    /**
     *  This method provides api to test for doing bulk status update
     * @param aDriver
     * @param cpName
     * @param status
     * @return Campaign object if successful;null otherwise
     */
    public CampaignPage bkStatusUpdate(WebDriver aDriver,String cpName,String status,WebElement setStatusLink) {
        StatusModal stModal = launchBulkStatusUpdateModal(aDriver,setStatusLink);
        if (stModal != null) {
            if (stModal.updateStatus(aDriver,cpName,status)) {
                return (new CampaignPage(aDriver,cpName));
            }
        }
        return null;
    }

    /**
     * This method is used to select all adsets, then open the bulk budget update modal
     * @param aDriver
     * @return Budget Modal object if successful;null otherwise
     */
    public BudgetModal launchBulkBudgetUpdateModal(WebDriver aDriver,String budgetModalTitle,WebElement setBudgetLink) {
        if (getActionsDropDown(aDriver)) {
            if (waitForElementVisible(aDriver,setBudgetLink)) {
                setBudgetLink.click();
                waitForPageLoaded(aDriver);
                return (new BudgetModal(aDriver,budgetModalTitle));
            }
        }
        return null;
    }

    /**
     * This method provides api to test for doing bulk budget update
     * @param aDriver
     * @param cpName
     * @param budget
     * @return Campaign object if successful;null otherwise
     */
    public CampaignPage bkBudgetUpdate(WebDriver aDriver,String cpName,Double budget,String budgetModalTitle,WebElement setBudgetLink) {
        BudgetModal bgModal = launchBulkBudgetUpdateModal(aDriver,budgetModalTitle,setBudgetLink);
        if (bgModal != null) {
            if (bgModal.bulkBudgetUpdate(aDriver,budget)) {
                return (new CampaignPage(aDriver,cpName));
            }
        }
        return null;
    }


    public EndDateModal launchBulkEndDateUpdateModal(WebDriver aDriver,WebElement setEndDateLink) {
        if (getActionsDropDown(aDriver)) {
            if (waitForElementVisible(aDriver,setEndDateLink)) {
                setEndDateLink.click();
                waitForPageLoaded(aDriver);
                return (new EndDateModal(aDriver));
            }
        }
        return null;
    }

    public CampaignPage bkEndDateUpdate(WebDriver aDriver,String cpName,String endDate,String endTime,WebElement setEndDateLink) {
        EndDateModal endDModal = launchBulkEndDateUpdateModal(aDriver,setEndDateLink);
        if (endDModal != null) {
            if (endDModal.updateEndDateTime(aDriver,endDate,endTime)) {
                return (new CampaignPage(aDriver,cpName));
            }
        }
        return null;
    }


}
