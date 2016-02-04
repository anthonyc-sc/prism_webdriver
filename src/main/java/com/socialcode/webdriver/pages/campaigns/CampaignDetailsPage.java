package com.socialcode.webdriver.pages.campaigns;

import com.socialcode.webdriver.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by anthonyc on 1/7/16.
 */
public class CampaignDetailsPage extends BasePage {
    private static Logger LOG = LoggerFactory.getLogger(CampaignDetailsPage.class);

    WebDriver driver;

    @FindBy(xpath = "//*[@id='details']/div/form/h4")
    protected WebElement pageHeader;

    @FindBy (id = "name")
    protected WebElement cpNameEdit;

    @FindBy(xpath = "//*[@class='flex-row']/div")
    protected WebElement platformItem;

    @FindBy(xpath = "//*[@class='flex-row']/div[2]")
    protected WebElement accountItem;

    @FindBy(xpath ="//*[@id='insertion-order']/div/div/div/a/span")
    protected WebElement insertionOrderSelection;

    @FindBy(id = "start_date")
    protected WebElement startDateDisplay;

    @FindBy(id = "end_date")
    protected WebElement endDateDisplay;

    @FindBy(id = "budget")
    protected WebElement budgetDisplay;

    @FindBy(id = "kpi_goal")
    protected WebElement kpiGoalDisplay;

    @FindBy(id = "kpi")
    protected WebElement kpiDisplay;

    @FindBy(id = "objective_select")
    protected WebElement objectiveSelection;

    @FindBy(id = "save-campaign")
    protected WebElement saveChangesButton;

    @FindBy(xpath = "//*[@id = 'save-campaign']/div")
    protected WebElement savedButtonText;

    public CampaignDetailsPage(WebDriver aDriver) {
        driver = aDriver;

        pageInitElements(driver,this);

        if (!isPageLoaded()) {
            assert false : "This is not Campaign Details Page";
        }

        LOG.info("VERIFIED - Campaign Details Page is loaded");
    }

    /**
     * Checks if page is loaded
     * @return true if page with correct header is loaded; false otherwise
     */
    public boolean isPageLoaded() {
        LOG.debug("Verifying Campaign Details Page is loaded");
        if (waitForElementVisible(driver,pageHeader)) {
            return pageHeader.getText().contains("CAMPAIGN DETAILS");
        }
        return false;
    }

    /**
     * On Campaign Details page, click 'Save Changes' button to save and transition to Campaign page
     * @param aDriver
     * @param cpName
     * @return CampaignPage object if Campaign Page is loaded; failure otherwise
     */
    public CampaignPage saveChanges(WebDriver aDriver,String cpName) {
        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }
        waitForAjax(aDriver);
        saveChangesButton.click();
        return (new CampaignPage(aDriver,cpName));
    }

    /**
     * Temporary workaround for page redirect issue after clicking 'Save Changes' button on the Campaign Detail Page
     * @param aDriver
     * @param cpName
     * @return true if 'Save Changes' button changed to 'Saved'; false otherwise
     */
    public boolean saveChangesNoRedirect(WebDriver aDriver,String cpName) {
        waitForAjax(aDriver);
        saveChangesButton.click();
        if (waitForElementPresence(aDriver,"//*[@id ='save-campaign']/div[text() = 'Saved']")) {
            return true;
        }
        return false;
    }

    /**
     * Checks for correct values for some web elements on the Campaign Details page
     * @param cpName
     * @param platform
     * @param account
     * @param io
     * @param startDate
     * @param endDate
     * @param budget
     * @param kpiGoal
     * @param kpi
     * @param objective
     * @return empty string if successful; error message when mismatch of expected value versus displayed value is encountered
     */
    public String checkCampaignDetails(String cpName,String platform,String account,String io, String startDate,String endDate,String budget,String kpiGoal,String kpi,String objective) {
        String dCpName = cpNameEdit.getAttribute("value");
        String dPlatform = platformItem.getText();
        String dAccount = accountItem.getText();
        String dStartDate = startDateDisplay.getAttribute("value");
        String dEndDate = endDateDisplay.getAttribute("value");
        String dBudget = budgetDisplay.getAttribute("value");
        String dObjective = (new Select(objectiveSelection)).getFirstSelectedOption().getText();
        String dKpiGoal = kpiGoalDisplay.getAttribute("value");
        String dKpi = (new Select(kpiDisplay)).getFirstSelectedOption().getText();

        if (!dCpName.contentEquals(cpName)) {
            return "Expect campaign name " + cpName + " ,but get " + dCpName;
        }

        if (!dPlatform.contentEquals(platform)) {
            return "Expect platform " + platform + " ,but get " + dPlatform;
        }

        if (account.contains("@")) {
            if (!account.startsWith(dAccount + "@")) {
                return "Expect account " + account + " ,but get " + dAccount;
            }
        } else {
            if (!account.contentEquals(dAccount)) {
                return "Expect account " + account + " ,but get " + dAccount;
            }
        }

        if (!dStartDate.contentEquals(startDate)) {
            return "Expect start date " + startDate + " ,but get " + dStartDate;
        }

        if (!dEndDate.contentEquals(endDate)) {
            return "Expect end date " + endDate + " ,but get " + dEndDate;
        }

        if (!dBudget.contentEquals(budget)) {
            return "Expect budget " + budget + " ,but get " + dBudget;
        }

        if (!dObjective.contentEquals(objective)) {
            return "Expect objective " + objective + " ,but get " + dObjective;
        }

        if (!dKpiGoal.contentEquals(kpiGoal)) {
            return "Expect KPI Goal " + kpiGoal + " ,but get " + kpiGoal;
        }

        if (!dKpi.contentEquals(kpi)) {
            return "Expect kpi " + kpi + " ,but get " + dKpi;
        }

        return "";
    }
}
