package com.socialcode.webdriver.pages.facebook;

import com.socialcode.webdriver.pages.bulk_update.BidModal;
import com.socialcode.webdriver.pages.bulk_update.BudgetModal;
import com.socialcode.webdriver.pages.bulk_update.EndDateModal;
import com.socialcode.webdriver.pages.bulk_update.StatusModal;
import com.socialcode.webdriver.pages.campaigns.CampaignPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by anthonyc on 1/25/16.
 */
public class FacebookCampaign extends CampaignPage {
    private static Logger LOG = LoggerFactory.getLogger(FacebookCampaign.class);

    WebDriver driver;

    @FindBy(xpath = "//*[@class = 'create-ad-nav']/i")
    protected WebElement fbIcon;

    @FindBy(xpath = "//*[@data-show-in-table = 'adsets']")
    protected WebElement adSetsTab;

    @FindBy(xpath = "//*[@class = 'table backgrid sc-big-datagrid']")
    protected WebElement adSetsTable;

    @FindBy(xpath = "//*[@class = 'table backgrid sc-big-datagrid']/thead")
    protected WebElement adSetsTableHeader;

    @FindBy(xpath = "//*[@class = 'table backgrid sc-big-datagrid']/tbody")
    protected WebElement adSetsTableBody;

    @FindBy(xpath = "//*[@class = 'select-all-header-cell']/div/button[2]")
    protected WebElement ActionsDropdown;

    @FindBy(xpath = "//*[@class = 'select-all-header-cell']/div/button")
    protected WebElement selectAllCheckBox;

    @FindBy(id = "set-status")
    protected WebElement setStatusLink;

    @FindBy(id = "set-budget")
    protected WebElement setBudgetLink;

    @FindBy(id = "set-bid")
    protected WebElement setBidLink;

    @FindBy(id = "set-end")
    protected WebElement setEndDateLink;


    public FacebookCampaign(WebDriver aDriver,String cpName) {
        super(aDriver,cpName);

        driver = aDriver;

        pageInitElements(driver,this);

        if (!isPageLoaded()) {
            assert false : "This is not Facebook Campaign Page";
        }

        LOG.info("VERIFIED - Facebook Campaign Page is loaded");
    }

    /**
     * Checks if Facebook Campaign Page is loaded
     * @return true if page is loaded with correct platform icon; false otherwise
     */
    public boolean isPageLoaded() {
        LOG.debug("Verifying Facebook Campaign Page is loaded");
        if (waitForElementVisible(driver,fbIcon)) {
            return fbIcon.getAttribute("class").contains("facebook");
        }
        return false;
    }

    /**
     *  This method is used to select all adsets, then open the bulk status update modal
     * @param aDriver
     * @return Status Modal object if successful;null otherwise
     */
    public StatusModal launchBulkStatusUpdateModal(WebDriver aDriver) {
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
     *  This method provides api to test for doing bulk status update
     * @param aDriver
     * @param cpName
     * @param status
     * @return Facebook Campaign object if successful;null otherwise
     */
    public FacebookCampaign bulkStatusUpdate(WebDriver aDriver,String cpName,String status) {
        StatusModal stModal = launchBulkStatusUpdateModal(aDriver);
        if (stModal != null) {
            if (stModal.updateStatus(aDriver,cpName,status)) {
                return (new FacebookCampaign(aDriver,cpName));
            }
        }
        return null;
    }

    /**
     * This method is used to select all adsets, then open the bulk budget update modal
     * @param aDriver
     * @return Budget Modal object if successful;null otherwise
     */
    public BudgetModal launchBulkBudgetUpdateModal(WebDriver aDriver) {
        if (getActionsDropDown(aDriver)) {
            if (waitForElementVisible(aDriver,setBudgetLink)) {
                setBudgetLink.click();
                waitForPageLoaded(aDriver);
                return (new BudgetModal(aDriver));
            }
        }
        return null;
    }

    /**
     * This method provides api to test for doing bulk budget update
     * @param aDriver
     * @param cpName
     * @param budget
     * @return Facebook Campaign object if successful;null otherwise
     */
    public FacebookCampaign bulkBudgetUpdate(WebDriver aDriver,String cpName,Double budget) {
        BudgetModal bgModal = launchBulkBudgetUpdateModal(aDriver);
        if (bgModal != null) {
            if (bgModal.bulkBudgetUpdate(aDriver,budget)) {
                return (new FacebookCampaign(aDriver,cpName));
            }
        }
        return null;
    }


    /**
     * This method is used to select all adsets, then open the bulk bid update modal
     * @param aDriver
     * @return BidM Modal object if successful;null otherwise
     */
    public BidModal launchBulkBidUpdateModal(WebDriver aDriver) {
        if (getActionsDropDown(aDriver)) {
            if (waitForElementVisible(aDriver,setBidLink)) {
                setBidLink.click();
                waitForPageLoaded(aDriver);
                return (new BidModal(aDriver));
            }
        }
        return null;
    }

    /**
     * This method provides api to test for doing bulk bid update
     * @param aDriver
     * @param cpName
     * @param bid
     * @return Facebook Campaign object if successful;null otherwise
     */
    public FacebookCampaign bulkBidUpdate(WebDriver aDriver,String cpName,Double bid) {
        BidModal bidModal = launchBulkBidUpdateModal(aDriver);
        if (bidModal != null) {
            if (bidModal.bulkBidUpdate(aDriver,bid)) {
                return (new FacebookCampaign(aDriver,cpName));
            }
        }
        return null;
    }

    public EndDateModal launchBulkEndDateUpdateModal(WebDriver aDriver) {
        if (getActionsDropDown(aDriver)) {
            if (waitForElementVisible(aDriver,setEndDateLink)) {
                setEndDateLink.click();
                waitForPageLoaded(aDriver);
                return (new EndDateModal(aDriver));
            }
        }
        return null;
    }

    public FacebookCampaign bulkEndDateUpdate(WebDriver aDriver,String cpName,String endDate,String endTime) {
        EndDateModal endDModal = launchBulkEndDateUpdateModal(aDriver);
        if (endDModal != null) {
            if (endDModal.updateEndDateTime(aDriver,endDate,endTime)) {
                return (new FacebookCampaign(aDriver,cpName));
            }
        }
        return null;
    }

    /**
     * Retrieve each of column values of an ad set for all the rows in the Ad Sets Table
     * @param aDriver
     * @return  list of rows of ad sets with individual column values if successful;null or empty list otherwise
     */
    public List<HashMap<String,String>> getAdSetsList(WebDriver aDriver) {
        List<HashMap<String,String>> adSetsList = new ArrayList<HashMap<String,String>>();
        try {
            if (waitForElementVisible(aDriver, adSetsTableBody)) {
                List<WebElement> rows = adSetsTableBody.findElements(By.tagName("tr"));
                for (WebElement r : rows) {
                    List<WebElement> columns = r.findElements(By.tagName("td"));
                    if (columns.size() != 11) {
                        return null;
                    }
                    HashMap<String, String> item = new HashMap<String, String>();

                    // Retrieve Ad Set Name
                    item.put("Name", columns.get(1).getText());

                    // Add Start Date and Time
                    WebElement elementStartD = columns.get(2).findElement(By.xpath("./span/div"));
                    item.put("Start Date",elementStartD.getText());

                    // Retrieve End Date and Time
                    WebElement elementEndD = columns.get(3).findElement(By.xpath("./span/div"));
                    item.put("End Date",elementEndD.getText());

                    // Retrieve Performance
                    item.put("Performance",columns.get(4).getText());

                    // Retrieve Results
                    item.put("Results",columns.get(5).getText());

                    // Retrieve Reach
                    item.put("Reach",columns.get(6).getText());

                    // Retrieve Max Bids
                    WebElement elementMaxBid = columns.get(7).findElement(By.xpath("./span"));
                    item.put("Max Bids",elementMaxBid.getText());

                    // Retrieve Budget
                    WebElement elementBudget = columns.get(8).findElement(By.xpath("./span"));
                    item.put("Budget",elementBudget.getText());

                    // Retrieve Spend
                    item.put("Spend",columns.get(9).getText());

                    // Retrieve Add Ad Set Status
                    WebElement elementStatus = columns.get(10).findElement(By.xpath(".//div/button/i"));
                    item.put("Status", elementStatus.getAttribute("title"));

                    adSetsList.add(item);
                }
            }
        } catch (Exception e) {
            return null;
        }

        return adSetsList;
    }


}
