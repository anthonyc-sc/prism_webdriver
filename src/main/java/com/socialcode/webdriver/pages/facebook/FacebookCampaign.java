package com.socialcode.webdriver.pages.facebook;

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
        if (waitForElementVisible(aDriver,selectAllCheckBox)) {
            selectAllCheckBox.click();
            if (waitForElementVisible(aDriver,ActionsDropdown)) {
                ActionsDropdown.click();
                if (waitForElementVisible(aDriver,setStatusLink)) {
                    setStatusLink.click();
                    waitForPageLoaded(aDriver);
                    return (new StatusModal(aDriver));
                }
            }
        }
        return null;
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
            return (stModal.updateStatus(aDriver,cpName,status));
        }
        return null;
    }

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

                    // Retrieve Ad Sets Name and add to the list
                    item.put("Name", columns.get(1).getText());

                    // Retrieve Ad Sets status and add to the list
                    WebElement element = columns.get(10).findElement(By.xpath(".//div/button/i"));
                    item.put("Status", element.getAttribute("title"));

                    adSetsList.add(item);
                }
            }
        } catch (Exception e) {
            return null;
        }

        return adSetsList;
    }


}
