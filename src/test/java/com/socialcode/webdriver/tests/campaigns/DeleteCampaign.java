package com.socialcode.webdriver.tests.campaigns;

import com.socialcode.webdriver.pages.campaigns.CampaignPage;
import com.socialcode.webdriver.pages.campaigns.CampaignsListPage;
import com.socialcode.webdriver.pages.campaigns.DeleteCampaignModal;
import com.socialcode.webdriver.pages.initiatives.InitiativePage;
import com.socialcode.webdriver.pages.initiatives.InitiativesListPage;
import com.socialcode.webdriver.pages.login.LoginPage;
import com.socialcode.webdriver.tests.CommonUtil;
import com.socialcode.webdriver.tests.TestData;
import com.socialcode.webdriver.tests.WebDriverSetup;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import static org.testng.Assert.assertNotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by anthonyc on 1/24/16.
 */
public class DeleteCampaign extends WebDriverSetup {
    private static Logger LOG = LoggerFactory.getLogger(DeleteCampaign.class);

    public DeleteCampaign() {
        data = new TestData();
        data.load(dataFolder + "campaigndatafordeletion_" + platF + ".xml");
    }

    @DataProvider(name = "getCampaigns")
    public Object[][] getCampaigns() {
        String[] cols = {"name","initiative_id","platform","account","insertion_order","total_budget","media_budget","objective", "kpi_goal", "kpi", "start_date", "end_date", "funding_instrument"};
        return data.getDataByElement("campaign",cols);
    }

    @DataProvider(name ="getDCampaign")
    public Object[][] getCampaignToDelete(){
        String[] cols = {"test","name"};
        return data.getDataByElement("campaign",cols);
    }

    @Test(description = "Test Setup - Create Campaigns For Deletion Test",groups = {"campaigns","delete_campaigns"},dataProvider = "getCampaigns")
    public void createCampaignForDeletion(String cpName,Integer initID,String platform,String account,String insertionOrder,Double totalBudget,Double mediaBudget,String objective,Double kpiGoal,String kpi,String sDateFlag,String endDateFlag,String fInstr) throws Exception {
        LOG.info("Starting createCampaignForDeletion.....");

        // Convert start and end date
        String startDate = CommonUtil.getDateByDuration(sDateFlag,0);
        String endDate = CommonUtil.getDateByDuration(endDateFlag,0);
        assertFalse(startDate.isEmpty(),"Start Date is empty.");
        assertFalse(endDate.isEmpty(),"End Date is empty.");

        // Navigate to Advisor-V2 application login screen
        driver.get(prismURL);

        // Log in with default username and password and verify initiative list page is displayed
        InitiativesListPage initListPage = (new LoginPage(driver)).enterLoginId(loginID).enterPassword(password).submit(driver);
        assertNotNull(initListPage,"Fail to login to Prism.");

        // Go to specific initiative with given initiative ID. Then create new campaign.
        InitiativePage initPage = initListPage.gotoInitiative(initID);
        Boolean result = initPage.createNewSCCampaginNoRedirect(driver,cpName,platform,account,insertionOrder,totalBudget,mediaBudget,objective,kpiGoal,kpi,startDate,endDate,fInstr);
        assertTrue(result);
    }

    @Test(description = "TC: Delete SC Campaign",groups = {"campaigns","delete_campaigns"},dependsOnMethods = { "createCampaignForDeletion" },dataProvider = "getDCampaign")
    public void delete_SC_Campaign(String testCase,String cpName) throws Exception {
        LOG.info("Starting delete_SC_Campaign......");

        // Navigate to Advisor-V2 application login screen
        driver.get(prismURL);

        // Log in with default username and password and verify initiative list page is displayed
        InitiativesListPage initListPage = (new LoginPage(driver)).enterLoginId(loginID).enterPassword(password).submit(driver);
        assertNotNull(initListPage,"Fail to login to Prism.");

        // Go to Campaign tab
        CampaignsListPage cpListPage = initListPage.goToCampaignTab(driver);
        assertNotNull(cpListPage);
        cpListPage.waitForPageLoaded(driver);
        cpListPage.waitForAjax(driver);

        // Search and go to campaign page
        CampaignPage cpPage = cpListPage.searchCampaignByName(driver,cpName);
        assertNotNull(cpPage);

        // Delete currently displayed campaign
        DeleteCampaignModal dcpModal = cpPage.deleteCampaign(driver);
        assertNotNull(dcpModal);

        // Confirm campaign deletion
        InitiativePage initPage = dcpModal.confirmDeletion(driver);
        assertNotNull(initPage);

        // Retrieve alert message and verify toast
        String alert = initPage.getAlertMessage(driver);
        assertTrue(alert.contains("Successfully deleted campaign"));

        // Close alert message 
        initPage.closeAlert(driver);
    }
}
