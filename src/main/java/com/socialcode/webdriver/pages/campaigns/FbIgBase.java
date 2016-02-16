package com.socialcode.webdriver.pages.campaigns;

import com.socialcode.webdriver.pages.bulk_update.BidModal;
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
 * Created by anthonyc on 2/4/16.
 */
public class FbIgBase extends CampaignPage {
    private static Logger LOG = LoggerFactory.getLogger(FbIgBase.class);

    private final String budgetModalTitle = "Lifetime budget";
    private final String dateModalTitle = "End Date";

    @FindBy(xpath = "//*[@data-show-in-table = 'adsets']")
    protected WebElement adSetsTab;

    @FindBy(xpath = "//*[@class = 'table backgrid sc-big-datagrid']/tbody")
    protected WebElement adSetsTableBody;

    @FindBy(id = "set-status")
    protected WebElement setStatusLink;

    @FindBy(id = "set-budget")
    protected WebElement setBudgetLink;

    @FindBy(id = "set-bid")
    protected WebElement setBidLink;

    @FindBy(id = "set-end")
    protected WebElement setEndDateLink;


    public FbIgBase(WebDriver aDriver,String cpName) {
        super(aDriver,cpName);
    }


    /**
     *  This method provides api to test for doing bulk status update
     * @param aDriver
     * @param cpName
     * @param status
     * @return Campaign object if successful;null otherwise
     */
    public FbIgBase bulkStatusUpdate(WebDriver aDriver,String cpName,String status) {
        if (bkStatusUpdate(aDriver,cpName,status,setStatusLink) != null) {
            return (new FbIgBase(aDriver,cpName));
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
    public FbIgBase bulkBudgetUpdate(WebDriver aDriver,String cpName,Double budget) {
        if (bkBudgetUpdate(aDriver,cpName,budget,budgetModalTitle,setBudgetLink) != null) {
            return (new FbIgBase(aDriver,cpName));
        }
        return null;
    }


    /**
     * This method is used to select all adsets, then open the bulk bid update modal
     * @param aDriver
     * @return Bid Modal object if successful;null otherwise
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
     * @return Campaign object if successful;null otherwise
     */
    public FbIgBase bulkBidUpdate(WebDriver aDriver,String cpName,Double bid) {
        BidModal bidModal = launchBulkBidUpdateModal(aDriver);
        if (bidModal != null) {
            if (bidModal.bulkBidUpdate(aDriver,bid)) {
                return (new FbIgBase(aDriver,cpName));
            }
        }
        return null;
    }

    public FbIgBase bulkEndDateUpdate(WebDriver aDriver,String cpName,String endDate,String endTime) {
        if (bkEndDateUpdate(aDriver,cpName,"",endDate,endTime,setEndDateLink,dateModalTitle) != null) {
            return (new FbIgBase(aDriver,cpName));
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
