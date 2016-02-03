package com.socialcode.webdriver.pages.initiatives;

import com.socialcode.webdriver.pages.BasePage;
import com.socialcode.webdriver.pages.campaigns.CampaignDetailsPage;
import com.socialcode.webdriver.pages.campaigns.CampaignPage;
import com.socialcode.webdriver.pages.campaigns.NewCampaignModal;
import com.socialcode.webdriver.pages.facebook.FacebookCampaign;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by anthonyc on 12/3/15.
 */
public class InitiativePage extends BasePage {
    private static Logger LOG = LoggerFactory.getLogger(InitiativePage.class);

    private WebDriver driver;

    @FindBy(className = "initiative-details-sc")
    protected WebElement initPanel;

    @FindBy(xpath = "//*[@class = 'sc-card-details']/div[2]/h2")
    protected WebElement initiativeHeader;

    @FindBy(xpath = "//span[text()='SocialCode']/../../div[2]/h2")
    protected WebElement pageHeader;

    @FindBy(xpath = "//span[text()='SocialCode']/../div/span")
    protected WebElement initStatus;

    @FindBy(xpath = "//span[text()='SocialCode']/../../div[3]/div")
    protected WebElement pacingLabel;

    @FindBy(xpath = "//span[text()='SocialCode']/../../div[3]/div[2]")
    protected WebElement pacingValue;

    @FindBy(xpath = "//span[text()='SocialCode']/../../div[4]/div")
    protected WebElement mediaSpendLabel;

    @FindBy(xpath = "//span[text()='SocialCode']/../../div[4]/div[2]")
    protected WebElement mediaSpendValue;

    @FindBy(xpath = "//span[text()='SocialCode']/../../div[5]/div")
    protected WebElement endingLabel;

    @FindBy(xpath = "//span[text()='SocialCode']/../../div[5]/div[2]")
    protected WebElement endingValue;

    @FindBy(xpath = "//*[@id='campaign-table']/div/div/table/thead/tr/th[1]/input")
    protected WebElement campaignTableCheckbox;

    @FindBy(xpath = "//*[@id='campaign-table']/div/div/table/thead/tr/th[2]/a")
    protected WebElement campaignHeader;

    @FindBy(xpath = "//*[@id='campaign-table']/div/div/table/thead/tr/th[3]/a")
    protected WebElement EndingHeader;

    @FindBy(xpath = "//*[@id='campaign-table']/div/div/table/thead/tr/th[4]/a")
    protected WebElement KPIHeader;

    @FindBy(xpath = "//*[@id='campaign-table']/div/div/table/thead/tr/th[5]/a")
    protected WebElement todayMSpendHeader;

    @FindBy(xpath = "//*[@id='campaign-table']/div/div/table/thead/tr/th[6]/a")
    protected WebElement totalMSpendHeader;

    @FindBy(xpath = "//*[@id='campaign-table']/div/div/table/thead/tr/th[7]/a")
    protected WebElement statusHeader;

    @FindBy (xpath = "//*[@id='initiative-overview']/div/div/h3")
    protected WebElement initDetailPanelHeader;

    @FindBy (xpath = "//*[@id='initiative-overview']/div/ul/li/div")
    protected WebElement initBudgetLabel;

    @FindBy (xpath = "//*[@id='initiative-overview']/div/ul/li/div[2]")
    protected WebElement initBudgetValue;

    @FindBy (xpath = "//*[@id='initiative-overview']/div/ul/li[2]/div")
    protected WebElement initDatesLabel;

    @FindBy (xpath = "//*[@id='initiative-overview']/div/ul/li[2]/div[2]")
    protected WebElement initDatesValue;

    @FindBy(xpath = "//*[@id='initiative-overview']/div/ul/li[3]/div")
    protected WebElement usersLabel;

    @FindBy(id = "create-campaign")
    protected WebElement createCampaignButton;

    @FindBy(xpath = "//*[@id = 'initiative-overview']/div/div/div/button")
    protected WebElement initDetailDropDownButton;

    @FindBy(id = "delete-initiative")
    protected WebElement deleteInitiativeLink;

    @FindBy(xpath = "//*[@id = 'alert-region']/div/div/div")
    protected WebElement alertMessages;

    @FindBy(xpath = "//*[@class ='toast-buttons']/a[2]")
    protected WebElement closeAlertLink;

    public InitiativePage(WebDriver d) {
        driver = d;

        pageInitElements(driver,this);

        if (!isPageLoaded()) {
            assert false : "This is not 'Initiative' page";
        }
        LOG.info("VERIFIED - 'Initiative' page is loaded");
    }

    public InitiativePage(WebDriver d,String initName) {
        driver = d;

        pageInitElements(driver,this);

        if (!isPageLoaded() || !initiativeHeader.getText().contentEquals(initName)) {
            assert false: "This is not 'Initiative' page for initiative " + initName;
        }
        LOG.info("VERIFIED - 'Initiative' page is loaded");
    }

    /**
     * Checks if 'Initiative' page is loaded
     * @return true if page is loaded; false otherwise
     */
    public boolean isPageLoaded() {
        LOG.debug("Verifying 'Initiative' page is loaded");
        return waitForElementVisible(driver,initPanel);
    }

    /**
     * Retrieves page header text
     * @return string containing value for page header;empty string if element is not visible
     */
    public String getPageHeader() {
        return isVisible(pageHeader)?pageHeader.getText():"";
    }

    /**
     * Retrieves pacing label
     * @return string containing pacing lable text;empty string is element is not visible
     */
    public String getPacingLabel() {
        return isVisible(pacingLabel)?pacingLabel.getText():"";
    }

    /**
     * Retrieves pacing value
     * @return string containing value for pacing value;empty string if element is not visible
     */
    public String getPacingValue() {
        return isVisible(pacingValue)?pacingValue.getText():"";
    }

    /**
     * Retrieves media spend label text
     * @return string containing media spend label text;empty string if element is not visible
     */
    public String getMediaSpendLabel() {
        return isVisible(mediaSpendLabel)?mediaSpendLabel.getText():"";
    }

    /**
     * Retrieves media spend value
     * @return string containing media spend value; empty string if element is not visible
     */
    public String getMediaSpendValue() {
        return isVisible(mediaSpendValue)?mediaSpendValue.getText():"";
    }

    /**
     * Retrieves ending label text
     * @return string containing ending label text;empty string if element is not visible
     */
    public String getEndingLabel() {
        return isVisible(endingLabel)?endingLabel.getText():"";
    }

    /**
     * Retrieves ending value
     * @return string containing ending value;empty string if element is not visible
     */
    public String getEndingValue() {
        return isVisible(endingValue)?endingValue.getText():"";
    }

    /**
     * Retrieves initiative status text
     * @return string containing initiative status;empty string if element is not visible
     */
    public String getInitStatus() {
        return isVisible(initStatus)?initStatus.getText():"";
    }

    /**
     * Retrieves initiative budget value
     * @return string containing initiative budget value;empty string if element is not visible
     */
    public String getInitBudget() {
        return isVisible(initBudgetValue)?initBudgetValue.getText():"";
    }

    /**
     * Retrieves initiative date range value
     * @return string containing initiative date range;empty string if element is not visible
     */
    public String getInitDateRange() {
        return isVisible(initDatesValue)?initDatesValue.getText():"";
    }

    /**
     * Launch New Campaign Modal
     * @param driver
     * @return New Campaign Modal object if successful;failure otherwise
     */
    public NewCampaignModal launchNewCampaignModal(WebDriver driver) {
        createCampaignButton.click();
        return (new NewCampaignModal(driver));
    }

    /**
     * Creates new Social Code Campaign
     * @param driver
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
     * @return Campaign Page object if successful;null otherwise
     * @throws Exception
     */
    public CampaignPage createNewSCCampaign(WebDriver driver,String cpName,String platform,String account,String insertionOrder,Double totalBudget,Double mediaBudget,String objective,Double kpiGoal,String kpi,String startDate,String endDate) throws Exception {
        NewCampaignModal cpModal = launchNewCampaignModal(driver);
        CampaignDetailsPage cpDetailPage =  cpModal.createNewSCCampaign(cpName,platform,account,insertionOrder,totalBudget,mediaBudget,objective,kpiGoal,kpi,startDate,endDate);

        // Verify values display on Campaign Details page
        String result = cpDetailPage.checkCampaignDetails(cpName,platform,account,insertionOrder,startDate,endDate,mediaBudget.toString(),kpiGoal.toString(),kpi,objective);
        if (cpDetailPage.checkCampaignDetails(cpName,platform,account,insertionOrder,startDate,endDate,mediaBudget.toString(),kpiGoal.toString(),kpi,objective).isEmpty()) {
            return cpDetailPage.saveChanges(driver,cpName);
        }
        return null;
    }

    /**
     * Delete initiative
     * @param aDriver
     * @return Initiative List Page object if successful;null otherwise
     */
    public InitiativesListPage deleteInitiative(WebDriver aDriver) {
        waitForPageLoaded(aDriver);
        waitForAjax(aDriver);
        if (waitForElementVisible(aDriver,initDetailDropDownButton)) {
            initDetailDropDownButton.click();
            if (waitForElementVisible(aDriver,deleteInitiativeLink)) {
                deleteInitiativeLink.click();
                DeleteInitiativeModal dModal = new DeleteInitiativeModal(aDriver);
                return (dModal.deleteConfirm(aDriver));
            }
        }
        return null;
    }

    /**
     * Temporary solution for creating campaign with no redirect
     * @param aDriver
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
     * @return true if successful;false otherwise
     * @throws Exception
     */
    public boolean createNewSCCampaginNoRedirect(WebDriver aDriver,String cpName,String platform,String account,String insertionOrder,Double totalBudget,Double mediaBudget,String objective,Double kpiGoal,String kpi,String startDate,String endDate) throws Exception {
        NewCampaignModal cpModal = launchNewCampaignModal(aDriver);
        if (platform.contentEquals("Pinterest")) {
            CampaignPage cpPage = cpModal.createNewPinterestSCCampaign(aDriver,cpName, platform, account, insertionOrder, totalBudget, mediaBudget, objective, kpiGoal, kpi, startDate, endDate);
            return (cpPage != null);
        } else {
            CampaignDetailsPage cpDetailPage = cpModal.createNewSCCampaign(cpName, platform, account, insertionOrder, totalBudget, mediaBudget, objective, kpiGoal, kpi, startDate, endDate);

            if (cpDetailPage != null) {
                // Verify values display on Campaign Details page
                String result = cpDetailPage.checkCampaignDetails(cpName, platform, account, insertionOrder, startDate, endDate, mediaBudget.toString(), kpiGoal.toString(), kpi, objective);
                String verifyResult = cpDetailPage.checkCampaignDetails(cpName, platform, account, insertionOrder, startDate, endDate, mediaBudget.toString(), kpiGoal.toString(), kpi, objective);
                System.out.println(verifyResult);
                if (verifyResult.isEmpty()) {
                    return cpDetailPage.saveChangesNoRedirect(aDriver, cpName);
                }
            }
        }
        return false;
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
            closeAlertLink.click();
            return true;
        }
        return false;
    }

    /**
     * This method used to access campaign page for specific campaign with given name in the input
     * @param aDriver
     * @param cpName
     * @return Facebook Campaign page object if successful;failure with error message otherwise
     */
    public FacebookCampaign goToFacebookCampaign(WebDriver aDriver, String cpName) {
        waitForPageLoaded(aDriver);
        if (waitForElementPresence(aDriver,"//a[text() = '"+cpName+"']")) {
            WebElement element = aDriver.findElement(By.xpath("//a[text() = '"+cpName+"']"));
            element.click();
            waitForAjax(aDriver);
            return (new FacebookCampaign(aDriver,cpName));
        }
        return null;
    }
}
