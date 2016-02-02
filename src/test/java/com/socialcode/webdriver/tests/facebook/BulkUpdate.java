package com.socialcode.webdriver.tests.facebook;

import com.socialcode.webdriver.pages.facebook.FacebookCampaign;
import com.socialcode.webdriver.pages.initiatives.InitiativePage;
import com.socialcode.webdriver.pages.initiatives.InitiativesListPage;
import com.socialcode.webdriver.pages.login.LoginPage;
import com.socialcode.webdriver.tests.CommonUtil;
import com.socialcode.webdriver.tests.TestData;
import com.socialcode.webdriver.tests.WebDriverSetup;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.testng.Assert.*;
import static org.testng.Assert.assertNotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by anthonyc on 1/25/16.
 */
public class BulkUpdate extends WebDriverSetup {
    private static Logger LOG = LoggerFactory.getLogger(BulkUpdate.class);

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

    @DataProvider(name = "getFBCampaignBid")
    public Object[][] getFBCampaignBid() {
        String[] cols = {"name","initiative_id","platform","bid"};
        return data.getDataByElement("fb_campaign_bid",cols);
    }

    @DataProvider(name = "getFBCampaignEndDate")
    public Object[][] getFBCampaignEndTime() {
        String[] cols = {"name","initiative_id","platform","end_date_offset","end_date_offset_unit","end_time"};
        return data.getDataByElement("fb_campaign_endtime",cols);
    }

    @Test(enabled = true,dataProvider = "getFBCampaignStatus")
    public void TC1_15_Bulk_Update_FB_AdStatus(String cpName,Integer initID,String platform,String statusText,String statusValue) throws Exception {
        LOG.info("Starting TC1_15_Bulk_Update_FB_AdStatus.....");

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
    }

    @Test(enabled = true,dataProvider = "getFBCampaignBudget")
    public void TC1_15_Bulk_Update_FB_AdBudget(String cpName,Integer initID,String platform,Double budget) throws Exception {
        LOG.info("Starting TC1_15_Bulk_Update_FB_AdBudget.....");

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

        // Perform bulk budget update
        fbCampaign = fbCampaign.bulkBudgetUpdate(driver,cpName,budget);
        assertNotNull(fbCampaign);
        fbCampaign.waitForPageLoaded(driver);

        // Verify success toast
        assertTrue(fbCampaign.getAlertMessage(driver).contains("Successfully updated lifetime budget to $" + budget),"Fail to verify success toast.");

        // Close alert box
        fbCampaign.closeAlert(driver);

        // Retrieve list of rows of adsets and verify budget in each row updated to the correct value
        List<HashMap<String,String>> adSets = fbCampaign.getAdSetsList(driver);
        assertNotNull(adSets);
        assertFalse(adSets.isEmpty(),"Ad Sets is empty for campaign" + cpName + "of initiative with id " + initID);

        for (HashMap<String,String> ad: adSets) {
            String budgetDisplay = ad.get("Budget").split("\n")[0];
            assertEquals(budgetDisplay,String.format("$%.2f",budget));
        }
        Thread.sleep(2000);
    }

    @Test(enabled = true,dataProvider = "getFBCampaignBid")
    public void TC1_15_Bulk_Update_FB_AdBid(String cpName,Integer initID,String platform,Double bid) throws Exception {
        LOG.info("Starting TC1_15_Bulk_Update_FB_AdBid.....");

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

        // Perform bulk bid update
        fbCampaign = fbCampaign.bulkBidUpdate(driver,cpName,bid);
        assertNotNull(fbCampaign);
        fbCampaign.waitForPageLoaded(driver);

        // Verify success toast
        String bidString = "";
        String[] bidSArray = String.format("%.2f",bid).split("\\.");
        if (bidSArray.length == 2) {
            bidString = bidSArray[0] + bidSArray[1];
        }
        assertTrue(fbCampaign.getAlertMessage(driver).contains("Successfully updated bid amount to " + bidString),"Fail to verify success toast for bulk bid update.");

        // Close alert box
        fbCampaign.closeAlert(driver);

        // Retrieve list of rows of adset and verify budget in each row updated to the correct value
        List<HashMap<String,String>> adSets = fbCampaign.getAdSetsList(driver);
        assertNotNull(adSets);
        assertFalse(adSets.isEmpty(),"Ad Sets is empty for campaign" + cpName + "of initiative with id " + initID);

        for (HashMap<String,String> ad: adSets) {
            String budgetDisplay = ad.get("Max Bids").split("\n")[0];
            assertEquals(budgetDisplay,String.format("$%.2f",bid));
        }
    }

    @Test(enabled = true,dataProvider = "getFBCampaignEndDate")
    public void TC1_15_Bulk_Update_FB_EndDate(String cpName,Integer initID,String platform,Integer endDateOffset,String endDateOffsetUnit,String endTime) {
        LOG.info("Starting TC1_15_Bulk_Update_FB_EndDate......");

        String endDate = CommonUtil.getDateByDuration(endDateOffsetUnit,endDateOffset);

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

        // Perform bulk end date
        fbCampaign = fbCampaign.bulkEndDateUpdate(driver,cpName,endDate,endTime);
        fbCampaign.waitForPageLoaded(driver);

        // Verify success toast
        assertTrue(fbCampaign.getAlertMessage(driver).contains("Successfully updated end time to "),"Fail to verify success toast");

        // Close alert box
        fbCampaign.closeAlert(driver);

        // Retrieve list of rows of adset and verify budget in each row updated to the correct value
        List<HashMap<String,String>> adSets = fbCampaign.getAdSetsList(driver);
        assertNotNull(adSets);
        assertFalse(adSets.isEmpty(),"Ad Sets is empty for campaign" + cpName + "of initiative with id " + initID);

        for (HashMap<String,String> ad: adSets) {
            assertTrue(ad.get("End Date").contains(CommonUtil.getDateStringByFormat(endDate,"M/d/yy") + "\n" + endTime));
        }
    }
}
