package com.socialcode.webdriver.pages.twitter;

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
 * Created by anthonyc on 2/10/16.
 */
public class TwitterCampaign extends CampaignPage {
    private static Logger LOG = LoggerFactory.getLogger(TwitterCampaign.class);

    private final String dailyBudgetModalTitle = "Daily budget";
    private final String totalBudgetModalTitle = "Total budget";
    private final String dateModalTitle = "Date";

    @FindBy(xpath = "//*[@class = 'table backgrid sc-big-datagrid']/tbody")
    protected WebElement cpTableBody;

    @FindBy(xpath = "//*[@class = 'create-ad-nav']/i")
    protected WebElement twitterIcon;

    @FindBy(id = "set-daily-budget")
    protected WebElement setDailyBudgetLink;

    @FindBy(id = "set-total-budget")
    protected WebElement setTotalBudgetLink;

    @FindBy(id = "set-dates")
    protected WebElement setDatesLink;

    @FindBy(id = "set-status")
    protected WebElement setStatusLink;



    public TwitterCampaign(WebDriver aDriver, String cpName) {
        super(aDriver,cpName);

        pageInitElements(aDriver,this);

        if (!isPageLoaded()) {
            assert false : "This is not Twitter Campaign Page";
        }

        LOG.info("VERIFIED - Twitter Campaign Page is loaded");
    }

    /**
     * Checks if Twitter Campaign Page is loaded
     * @return true if page is loaded with correct platform icon; false otherwise
     */
    public boolean isPageLoaded() {
        LOG.debug("Verifying Twitter Campaign Page is loaded");
        if (waitForElementVisible(driver,twitterIcon)) {
            return twitterIcon.getAttribute("class").contains("twitter");
        }
        return false;
    }

    public TwitterCampaign bulkDailyBudgetUpdate(WebDriver aDriver,String cpName,Double dBudget) {
        if (bkBudgetUpdate(aDriver,cpName,dBudget,dailyBudgetModalTitle,setDailyBudgetLink) != null) {
            return (new TwitterCampaign(aDriver,cpName));
        }
        return null;
    }

    public TwitterCampaign bulkTotalBudgetUpdate(WebDriver aDriver,String cpName,Double tBudget) {
        if (bkBudgetUpdate(aDriver,cpName,tBudget,totalBudgetModalTitle,setTotalBudgetLink) != null) {
            return (new TwitterCampaign(aDriver,cpName));
        }
        return null;
    }

    public TwitterCampaign bulkDatesUpdate(WebDriver aDriver,String cpName,String startDate,String endDate) {
        if (bkEndDateUpdate(aDriver,cpName,startDate,endDate,"",setDatesLink,dateModalTitle) != null) {
            return (new TwitterCampaign(aDriver,cpName));
        }
        return null;
    }

    public TwitterCampaign bulkStatusUpdate(WebDriver aDriver,String cpName,String status) {
        if (bkStatusUpdate(aDriver,cpName,status,setStatusLink) != null) {
            return (new TwitterCampaign(aDriver,cpName));
        }
        return null;
    }

    /**
     * Retrieve each of column values of an ad set for all the rows in the Ad Sets Table
     * @param aDriver
     * @return  list of rows of ad sets with individual column values if successful;null or empty list otherwise
     */
    public List<HashMap<String,String>> getCampaignList(WebDriver aDriver) {
        List<HashMap<String,String>> cpList = new ArrayList<HashMap<String,String>>();
        try {
            if (waitForElementVisible(aDriver, cpTableBody)) {
                List<WebElement> rows = cpTableBody.findElements(By.tagName("tr"));
                for (WebElement r : rows) {
                    List<WebElement> columns = r.findElements(By.tagName("td"));
                    if (columns.size() != 10) {
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

                    // Retrieve Daily Budget
                    WebElement elementDBudget = columns.get(4).findElement(By.xpath("./span"));
                    item.put("Daily Budget",elementDBudget.getText());

                    // Retrieve Total Budget
                    WebElement elementTBudget = columns.get(5).findElement(By.xpath("./span"));
                    item.put("Total Budget",elementTBudget.getText());

                    // Retrieve Spend
                    item.put("Spend",columns.get(6).getText());

                    // Retrieve Results
                    item.put("Results",columns.get(7).getText());

                    // Retrieve Performance
                    item.put("Performance",columns.get(8).getText());

                    // Retrieve Add Ad Set Status
                    WebElement elementStatus = columns.get(9).findElement(By.xpath(".//div/button/i"));
                    item.put("Status", elementStatus.getAttribute("title"));

                    cpList.add(item);
                }
            }
        } catch (Exception e) {
            return null;
        }

        return cpList;
    }
}
