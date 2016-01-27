package com.socialcode.webdriver.tests.facebook;

import com.socialcode.webdriver.pages.facebook.FacebookCampaign;
import com.socialcode.webdriver.pages.initiatives.InitiativePage;
import com.socialcode.webdriver.pages.initiatives.InitiativesListPage;
import com.socialcode.webdriver.pages.login.LoginPage;
import com.socialcode.webdriver.tests.TestData;
import com.socialcode.webdriver.tests.WebDriverSetup;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

import static org.testng.Assert.*;

import static org.testng.Assert.assertNotNull;

/**
 * Created by anthonyc on 1/25/16.
 */
public class BulkUpdate extends WebDriverSetup {
    public BulkUpdate() {
        data = new TestData();
        data.load("/data/fb_bulkupdate_data.xml");
    }

    @DataProvider(name = "getFBCampaignStatus")
    public Object[][] getFBCampaignStatus() {
        String[] cols = {"name","initiative_id","platform","status_text","status_value"};
        return data.getDataByElement("fb_campaign_status",cols);
    }

    @DataProvider(name = "getFBCampaignBudget")
    public Object[][] getFBCampaignBudget() {
        String[] cols = {"name","initiative_id","platform","budget"};
        return data.getDataByElement("fb_campaign_budget",cols);
    }

    @Test(enabled = true,dataProvider = "getFBCampaignStatus")
    public void bulkUpdateStatusTest(String cpName,Integer initID,String platform,String statusText,String statusValue) throws Exception {
        // Navigate to Advisor-V2 application login screen
        driver.get(prismURL);

        // Log in with default username and password and verify initiative list page is displayed
        InitiativesListPage initListPage = (new LoginPage(driver)).enterLoginId(loginID).enterPassword(password).submit();
        assertNotNull(initListPage,"Fail to login to Prism.");
        initListPage.waitForPageLoaded(driver);

        // Go to specific initiative having initiative id 'initID'
        InitiativePage initPage = initListPage.gotoInitiative(initID);
        initPage.waitForPageLoaded(driver);

        // Go to specific campaign for bulk update
        FacebookCampaign fbCampaign = initPage.goToFacebookCampaign(driver,cpName);
        fbCampaign.waitForPageLoaded(driver);

        // Perform bulk status update
        fbCampaign = fbCampaign.bulkStatusUpdate(driver,cpName,statusText);
        assertNotNull(fbCampaign);

        // Verify success toast
        assertTrue(fbCampaign.getAlertMessage(driver).contains("Successfully updated campaign status to " + statusValue));

        // Close alert box
        fbCampaign.closeAlert(driver);

        // Go through each of the ad sets in the Ad Sets table and verify their statuses are updated correctly
        List<HashMap<String,String>> adSets = fbCampaign.getAdSetsList(driver);
        assertNotNull(adSets);
        assertFalse(adSets.isEmpty(),"Ad Sets is empty for campaign" + cpName + "of initiative with id " + initID);

        for (HashMap<String,String> h: adSets) {
            if (!h.get("Status").equalsIgnoreCase(statusValue)) {
                fail("Expect staus of campaign " + cpName + " to be " + statusValue + ",but get " + h.get("Status"));
            }
        }
        Thread.sleep(3000);
    }

    @Test(enabled = false,dataProvider = "getFBCampaignBudget")
    public void bulkUpdateBudgetTest(String cpName,Integer initID,String platform,Double budget) {
        // Navigate to Advisor-V2 application login screen
        driver.get(prismURL);

        // Log in with default username and password and verify initiative list page is displayed
        InitiativesListPage initListPage = (new LoginPage(driver)).enterLoginId(loginID).enterPassword(password).submit();
        assertNotNull(initListPage,"Fail to login to Prism.");
        initListPage.waitForPageLoaded(driver);

        // Go to specific initiative having initiative id 'initID'
        InitiativePage initPage = initListPage.gotoInitiative(initID);
        initPage.waitForPageLoaded(driver);

        // Go to specific campaign for bulk update
        FacebookCampaign fbCampaign = initPage.goToFacebookCampaign(driver,cpName);
        fbCampaign.waitForPageLoaded(driver);

        // To-Do
    }



}
