package com.socialcode.webdriver.pages.pinterest;

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
 * Created by anthonyc on 2/5/16.
 */
public class PinterestCampaign extends CampaignPage {
    private static Logger LOG = LoggerFactory.getLogger(PinterestCampaign.class);

    private final String budgetModalTitle = "Daily budget";
    private final String dateModalTitle = "End Date";

    @FindBy(xpath = "//*[@class = 'create-ad-nav']/i")
    protected WebElement pinterestIcon;

    @FindBy(id = "report")
    protected WebElement getReportLink;

    @FindBy(id = "bulk")
    protected WebElement getBulkSheetLink;

    @FindBy(xpath = "//*[@class = 'table backgrid sc-big-datagrid']/tbody")
    protected WebElement adsTableBody;

    @FindBy(id = "budget")
    protected WebElement setBudgetLink;

    @FindBy(id = "end-date")
    protected WebElement setEndDateLink;

    @FindBy(id = "status")
    protected WebElement setStatusLink;


    public PinterestCampaign(WebDriver aDriver, String cpName) {
        super(aDriver,cpName);

        pageInitElements(aDriver,this);

        if (!isPageLoaded()) {
            assert false : "This is not Pinterest Campaign Page";
        }

        LOG.info("VERIFIED - Pinterest Campaign Page is loaded");
    }

    /**
     * Checks if Pinterest Campaign Page is loaded
     * @return true if page is loaded with correct platform icon; false otherwise
     */
    public boolean isPageLoaded() {
        LOG.debug("Verifying Pinterest Campaign Page is loaded");
        if (waitForElementVisible(driver,pinterestIcon)) {
            return pinterestIcon.getAttribute("class").contains("pinterest");
        }
        return false;
    }

    public PinterestCampaign bulkBudgetUpdate(WebDriver aDriver,String cpName,Double budget) {
        if (bkBudgetUpdate(aDriver,cpName,budget,budgetModalTitle,setBudgetLink) != null) {
            return (new PinterestCampaign(aDriver,cpName));
        }
        return null;
    }

    public PinterestCampaign bulkEndDateUpdate(WebDriver aDriver,String cpName,String endDate,String endTime) {
        if (bkEndDateUpdate(aDriver,cpName,"",endDate,endTime,setEndDateLink,dateModalTitle) != null) {
            return (new PinterestCampaign(aDriver,cpName));
        }
        return null;
    }

    public PinterestCampaign bulkStatusUpdate(WebDriver aDriver,String cpName,String status) {
        if (bkStatusUpdate(aDriver,cpName,status,setStatusLink) != null) {
            return (new PinterestCampaign(aDriver,cpName));
        }
        return null;
    }

    /**
     * Retrieve each of column values of an ad for all the rows in the Ads Table
     * @param aDriver
     * @return  list of rows of ad sets with individual column values if successful;null or empty list otherwise
     */
    public List<HashMap<String,String>> getAdsList(WebDriver aDriver) {
        List<HashMap<String,String>> adsList = new ArrayList<HashMap<String,String>>();
        try {
            if (waitForElementVisible(aDriver, adsTableBody)) {
                List<WebElement> rows = adsTableBody.findElements(By.tagName("tr"));
                for (WebElement r : rows) {
                    List<WebElement> columns = r.findElements(By.tagName("td"));
                    if (columns.size() != 7) {
                        return null;
                    }
                    HashMap<String, String> item = new HashMap<String, String>();

                    // Retrieve Ad Name
                    item.put("Name", columns.get(1).getText());

                    // Add End Date
                    WebElement elementEndD = columns.get(2).findElement(By.xpath("./span/div"));
                    item.put("End Date",elementEndD.getText());

                    // Retrieve Bid Type
                    item.put("Bid Type",columns.get(3).getText());

                    // Retrieve Pin Promotions
                    item.put("Pin Promotions",columns.get(4).getText());

                    // Retrieve Daily Budget
                    WebElement elementDailyBudget = columns.get(5).findElement(By.xpath("./span"));
                    item.put("Daily Budget",elementDailyBudget.getText());

                    // Retrieve Ad Status
                    WebElement elementStatus = columns.get(6).findElement(By.xpath("./div/button/i"));
                    item.put("Status", elementStatus.getAttribute("title"));

                    adsList.add(item);
                }
            }
        } catch (Exception e) {
            return null;
        }

        return adsList;
    }

}
