package com.socialcode.webdriver.pages.campaigns;

import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptExecutor;
import com.socialcode.webdriver.pages.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anthonyc on 12/14/15.
 */
public class NewCampaignModal extends BasePage {
    private static Logger LOG = LoggerFactory.getLogger(NewCampaignModal.class);

    WebDriver driver;

    @FindBy(id = "sc-modal-title")
    protected WebElement modalTitle;

    @FindBy(xpath = "//input[@id='name']/../label")
    protected WebElement campaignNameLabel;

    @FindBy(id = "name")
    protected WebElement campaignNameEdit;

    @FindBy(xpath = "//*[@id='platform']/../label")
    protected WebElement platformLabel;

    @FindBy(id = "platform")
    protected WebElement platformCBox;

    @FindBy(xpath = "//*[@id='account_name']/../label")
    protected WebElement accountNameLabel;

    @FindBy(id = "account_name")
    protected WebElement accountNameCBox;

    @FindBy(id = "s2id_insertion_order")
    protected WebElement insertionOrderBox;

    @FindBy(xpath = "//*[@id='s2id_insertion_order']/../label")
    protected WebElement insertionOrderLabel;

    @FindBy(xpath = "//*[@class = 'select2-search']/input")
    protected WebElement insertionOrderSearchEdit;

    @FindBy(id = "total_budget")
    protected WebElement totalBudgetEdit;

    @FindBy(id = "budget")
    protected WebElement mediaSpend;

    @FindBy(id = "objective")
    protected WebElement objectiveCBox;

    @FindBy(id = "kpi_goal")
    protected WebElement kpiGoalEdit;

    @FindBy(id = "kpi")
    protected WebElement kpiCBox;

    @FindBy(id = "start_date")
    protected WebElement startDateEdit;

    @FindBy(id = "end_date")
    protected WebElement endDateEdit;

    @FindBy(id = "submit")
    protected WebElement submitButton;

    @FindBy(id = "cancel")
    protected WebElement cancelButton;


    public NewCampaignModal(WebDriver aDriver) {
        driver = aDriver;

        pageInitElements(driver,this);

        if (!isPageLoaded()) {
            assert false : "This is not New Campaign Modal";
        }
        LOG.info("VERIFIED - New Campaign Modal is loaded");
    }

    /**
     * Checks if New Campaign Modal is loaded
     * @return true if modal with correct title is displayed; false otherwise
     */
    public boolean isPageLoaded() {
        LOG.debug("Verifying New Campaign Modal is loaded");
        if (waitForElementVisible(driver,modalTitle)) {
            return modalTitle.getText().contains("New Campaign");
        }
        return false;
    }

    /**
     * Enters specified values on the web elements of the New Campaign Modal
     * @param cpName
     * @param platform
     * @param account
     * @param insertionOrder
     * @param totalBudget
     * @param mediaBudget
     * @param objective
     * @param kpiGoal
     * @param kpi
     * @param startDate
     * @param endDate
     * @return empty string if successful; error message otherwise
     * @throws Exception - this is for time out exception from thread.sleep
     */
    public String enterCampaignData(String cpName,String platform,String account,String insertionOrder,Double totalBudget,Double mediaBudget,String objective,Double kpiGoal,String kpi,String startDate,String endDate)
      throws Exception {

        waitForAjax(driver);
        Thread.sleep(5000);

        if (!type(campaignNameEdit,cpName).contentEquals(cpName)) {
            return "Unable to enter campaign name";
        }

        if (!selectByText(platformCBox,platform).contentEquals(platform)) {
            return "Unable to select value for platform";
        }

        waitForAjax(driver);
        if (!selectByText(accountNameCBox,account).contentEquals(account)) {
            return "Unable to select value for account";
        }

        if (!inputInsertionOrder(insertionOrder)) {
            return "Unable to select Insertion Order";
        }

        if (!type(totalBudgetEdit,totalBudget.toString()).contentEquals(totalBudget.toString())) {
            return "Unable to enter Total Budget";
        }

        if (!selectByText(objectiveCBox,objective).contentEquals(objective)) {
            return "Unable to select objective";
        }

        if (!type(kpiGoalEdit,kpiGoal.toString()).contentEquals(kpiGoal.toString())) {
            return "Unable to enter KPI Goal";
        }

        if (!selectByText(kpiCBox,kpi).contentEquals(kpi)) {
            return "Unable to select kpi";
        }

        if (!type(startDateEdit,startDate).contentEquals(startDate)) {
            return "Unable to enter Start Date";
        }

        if (!type(endDateEdit,endDate).contentEquals(endDate)) {
            return "Unable to enter End Date";
        }
        return "";
    }

    /**
     * Search and select the given input insertion order on the New Campaign Modal
     * @param io
     * @return true if successful; false otherwise
     */
    public boolean inputInsertionOrder(String io) {
        insertionOrderBox.click();
        insertionOrderSearchEdit.sendKeys(io);
        if (waitForElementPresence(driver,"//div[text()='"+io+"']")) {
            WebElement element = driver.findElement(By.xpath("//div[text()='"+io+"']"));
            if (isVisible(element)) {
                element.click();
            }
            return true;
        }
        return false;
    }

    /**
     * Clicks Submit button on the New Campaign Modal
     * @return Campaign Details Page object
     */
    public CampaignDetailsPage submit() {
        submitButton.click();
        return (new CampaignDetailsPage(driver));
    }

    /**
     * Method invoke to create new Social Code campaign. It enters specified data, then clicks submit.
     * @param cpName
     * @param platform
     * @param account
     * @param insertionOrder
     * @param totalBudget
     * @param mediaBudget
     * @param objective
     * @param kpiGoal
     * @param kpi
     * @param startDate
     * @param endDate
     * @return Campaign Details Page object if successful; otherwise null
     * @throws Exception
     */
    public CampaignDetailsPage createNewSCCampaign(String cpName,String platform,String account,String insertionOrder,Double totalBudget,Double mediaBudget,String objective,Double kpiGoal,String kpi,String startDate,String endDate) throws Exception {
        String result = enterCampaignData(cpName,platform,account,insertionOrder,totalBudget,mediaBudget,objective,kpiGoal,kpi,startDate,endDate);
        if (result.isEmpty()) {
            return submit();
        }
        return null;
    }

}
