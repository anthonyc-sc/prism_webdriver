package com.socialcode.webdriver.tests.campaigns;

import com.socialcode.webdriver.pages.campaigns.CampaignDetailsPage;
import com.socialcode.webdriver.pages.campaigns.CampaignPage;
import com.socialcode.webdriver.pages.campaigns.NewCampaignModal;
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
 * Created by anthonyc on 12/14/15.
 */
public class CreateCampaign  extends WebDriverSetup {
    private static Logger LOG = LoggerFactory.getLogger(CreateCampaign.class);

    public CreateCampaign() {
        data = new TestData();
        data.load(dataFolder + "campaigndata.xml");
    }

    @DataProvider(name = "getCampaigns")
    public Object[][] getCampaigns() {
        String[] cols = {"test","name","initiative_id","platform","account","insertion_order","total_budget","media_budget","objective", "kpi_goal", "kpi", "start_date", "end_date","funding_instrument"};
        return data.getDataByElement("campaign",cols);
    }

    @Test(enabled = true,dataProvider = "getCampaigns")
    public void create_SC_Campaign(String testCase,String cpName,Integer initID,String platform,String account,String insertionOrder,Double totalBudget,Double mediaBudget,String objective,Double kpiGoal,String kpi,String sDateFlag,String endDateFlag,String fInstr) throws Exception {
        LOG.info("Starting TC1_12_Create_SC_Campaign.....");

        // Convert start and end date
        String startDate = CommonUtil.getDateByDuration(sDateFlag,0);
        String endDate = CommonUtil.getDateByDuration(endDateFlag,0);
        assertFalse(startDate.isEmpty(),"Start Date is empty.");
        assertFalse(endDate.isEmpty(),"End Date is empty.");

        // Navigate to Advisor-V2 application login screen
        driver.get(prismURL);

        // Log in with default username and password and verify initiative list page is displayed
        InitiativesListPage initListPage = (new LoginPage(driver)).enterLoginId(loginID).enterPassword(password).submit();
        assertNotNull(initListPage,"Fail to login to Prism.");

        // Go to specific initiative with given initiative ID. Then create new campaign.
        InitiativePage initPage = initListPage.gotoInitiative(initID);

       // CampaignPage cpPage = initPage.createNewSCCampaign(driver,cpName,platform,account,insertionOrder,totalBudget,mediaBudget,objective,kpiGoal,kpi,startDate,endDate);
        //assertNotNull(cpPage);

        // Temporary work around for issue ADV-3177
        Boolean result = initPage.createNewSCCampaginNoRedirect(driver,cpName,platform,account,insertionOrder,totalBudget,mediaBudget,objective,kpiGoal,kpi,startDate,endDate,fInstr);
        assertTrue(result);
    }

}
