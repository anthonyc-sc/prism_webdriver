package com.socialcode.webdriver.tests.facebook;

import com.socialcode.webdriver.pages.campaigns.FbIgBase;
import com.socialcode.webdriver.pages.initiatives.InitiativePage;
import com.socialcode.webdriver.pages.initiatives.InitiativesListPage;
import com.socialcode.webdriver.pages.login.LoginPage;
import com.socialcode.webdriver.pages.pinterest.PinterestCampaign;
import com.socialcode.webdriver.tests.CommonUtil;
import com.socialcode.webdriver.tests.TestData;
import com.socialcode.webdriver.tests.WebDriverSetup;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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
        data.load("/data/bulkupdate_data.xml");
    }

    @DataProvider(name = "getCampaignStatus")
    public Object[][] getCampaignStatus() {
        String[] cols = {"name","initiative_id","platform","status_text","status_value"};
        return data.getDataByElement("campaign_status",cols);
    }

    @DataProvider(name = "getCampaignBudget")
    public Object[][] getCampaignBudget() {
        String[] cols = {"name","initiative_id","platform","budget"};
        return data.getDataByElement("campaign_budget",cols);
    }

    @DataProvider(name = "getCampaignBid")
    public Object[][] getCampaignBid() {
        String[] cols = {"name","initiative_id","platform","bid"};
        return data.getDataByElement("campaign_bid",cols);
    }

    @DataProvider(name = "getCampaignEndDate")
    public Object[][] getCampaignEndTime() {
        String[] cols = {"name","initiative_id","platform","end_date_offset","end_date_offset_unit","end_time"};
        return data.getDataByElement("campaign_endtime",cols);
    }

    @Test(enabled = true,dataProvider = "getCampaignStatus")
    public void bulk_Update_AdStatus(String cpName,Integer initID,String platform,String statusText,String statusValue) throws Exception {
        LOG.info("Starting bulk_Update_AdStatus.....");

        // Navigate to Advisor-V2 application login screen
        driver.get(prismURL);

        // Log in with default username and password and verify initiative list page is displayed
        InitiativesListPage initListPage = (new LoginPage(driver)).enterLoginId(loginID).enterPassword(password).submit();
        assertNotNull(initListPage,"Fail to login to Prism.");
        initListPage.waitForPageLoaded(driver);

        // Go to specific initiative having initiative id 'initID'
        InitiativePage initPage = initListPage.gotoInitiative(initID);
        initPage.waitForPageLoaded(driver);

        switch(platform) {
            case "Facebook":
            case "Instagram":
                // Go to specific campaign for bulk update
                FbIgBase fbIgCampaign = initPage.goToCampaign(driver,cpName);
                fbIgCampaign.waitForPageLoaded(driver);

                // Perform bulk status update
                fbIgCampaign = fbIgCampaign.bulkStatusUpdate(driver,cpName,statusText);
                assertNotNull(fbIgCampaign);

                // Verify success toast
                assertTrue(fbIgCampaign.getAlertMessage(driver).contains("Successfully updated campaign status to " + statusValue));

                // Close alert box
                fbIgCampaign.closeAlert(driver);

                // Go through each of the ad sets in the Ad Sets table and verify their statuses are updated correctly
                List<HashMap<String,String>> adSets = fbIgCampaign.getAdSetsList(driver);
                assertNotNull(adSets);
                assertFalse(adSets.isEmpty(),"Ad Sets is empty for campaign" + cpName + "of initiative with id " + initID);

                for (HashMap<String,String> h: adSets) {
                    if (!h.get("Status").equalsIgnoreCase(statusValue)) {
                        fail("Expect status of campaign " + cpName + " to be " + statusValue + ",but get " + h.get("Status"));
                    }
                }
                break;
            case "Pinterest":
                // Go to specific campaign for bulk update
                PinterestCampaign pinCampaign = initPage.goToPinterestCampaign(driver,cpName);
                pinCampaign.waitForPageLoaded(driver);

                // Perform bulk status update
                pinCampaign = pinCampaign.bulkStatusUpdate(driver,cpName,statusText);
                assertNotNull(pinCampaign);

                // Verify success toast
                assertTrue(pinCampaign.getAlertMessage(driver).contains("Successfully updated status to " + statusValue));

                // Close alert box
                pinCampaign.closeAlert(driver);

                // Go through each of the ad sets in the Ad Sets table and verify their statuses are updated correctly
                List<HashMap<String,String>> ads = pinCampaign.getAdsList(driver);
                assertNotNull(ads);
                assertFalse(ads.isEmpty(),"Ads List is empty for campaign" + cpName + "of initiative with id " + initID);

                for (HashMap<String,String> h: ads) {
                    if (!h.get("Status").equalsIgnoreCase(statusValue)) {
                        fail("Expect status of campaign " + cpName + " to be " + statusValue + ",but get " + h.get("Status"));
                    }
                }
                break;
            default:
                fail("Invalid value for platform. Expect platform to be one of the following: Facebook, Instagram, Pinterest, or Twitter.");
        }

    }

    @Test(enabled = true,dataProvider = "getCampaignBudget")
    public void bulk_Update_AdBudget(String cpName,Integer initID,String platform,Double budget) throws Exception {
        LOG.info("Starting bulk_Update_AdBudget.....");

        // Navigate to Advisor-V2 application login screen
        driver.get(prismURL);

        // Log in with default username and password and verify initiative list page is displayed
        InitiativesListPage initListPage = (new LoginPage(driver)).enterLoginId(loginID).enterPassword(password).submit();
        assertNotNull(initListPage,"Fail to login to Prism.");
        initListPage.waitForPageLoaded(driver);

        // Go to specific initiative having initiative id 'initID'
        InitiativePage initPage = initListPage.gotoInitiative(initID);
        initPage.waitForPageLoaded(driver);

        switch(platform) {
            case "Facebook":
            case "Instagram":
                // Go to specific campaign for bulk update
                FbIgBase fbIgCampaign = initPage.goToCampaign(driver,cpName);
                fbIgCampaign.waitForPageLoaded(driver);

                // Perform bulk budget update
                fbIgCampaign = fbIgCampaign.bulkBudgetUpdate(driver,cpName,budget);
                assertNotNull(fbIgCampaign);
                fbIgCampaign.waitForPageLoaded(driver);

                // Verify success toast
                assertTrue(fbIgCampaign.getAlertMessage(driver).contains("Successfully updated lifetime budget to $" + budget),"Fail to verify success toast.");

                // Close alert box
                fbIgCampaign.closeAlert(driver);

                // Retrieve list of rows of adsets and verify budget in each row updated to the correct value
                List<HashMap<String,String>> adSets = fbIgCampaign.getAdSetsList(driver);
                assertNotNull(adSets);
                assertFalse(adSets.isEmpty(),"Ad Sets is empty for campaign" + cpName + "of initiative with id " + initID);

                for (HashMap<String,String> ad: adSets) {
                    String budgetDisplay = ad.get("Budget").split("\n")[0];
                    assertEquals(budgetDisplay,String.format("$%.2f",budget));
                }
                break;
            case "Pinterest":
                // Go to specific campaign for bulk update
                PinterestCampaign pnCampaign = initPage.goToPinterestCampaign(driver,cpName);
                pnCampaign.waitForPageLoaded(driver);

                pnCampaign.bulkBudgetUpdate(driver,cpName,budget);
                assertNotNull(pnCampaign);
                pnCampaign.waitForPageLoaded(driver);

                // Verify success toast
                assertTrue(pnCampaign.getAlertMessage(driver).contains("Successfully updated budget to " + String.format("$%.2f",budget)),"Fail to verify success toast.");

                // Close alert box
                pnCampaign.closeAlert(driver);

                // Retrieve list of rows of adsets and verify budget in each row updated to the correct value
                List<HashMap<String,String>> adsList = pnCampaign.getAdsList(driver);
                assertNotNull(adsList);
                assertFalse(adsList.isEmpty(),"Ad list is empty for campaign" + cpName + "of initiative with id " + initID);

                for (HashMap<String,String> ad: adsList) {
                    String budgetDisplay = ad.get("Daily Budget");
                    assertEquals(budgetDisplay,String.format("$%.2f",budget));
                }
                break;
            default:
                fail("Invalid value for platform. Expect to be one of the following: Facebook, Instagram,Pinterest or Twitter.");
        }
    }

    @Test(enabled = true,dataProvider = "getCampaignBid")
    public void bulk_Update_AdBid(String cpName,Integer initID,String platform,Double bid) throws Exception {
        LOG.info("Starting bulk_Update_AdBid.....");

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
        FbIgBase fbIgCampaign = initPage.goToCampaign(driver,cpName);
        fbIgCampaign.waitForPageLoaded(driver);

        // Perform bulk bid update
        fbIgCampaign = fbIgCampaign.bulkBidUpdate(driver,cpName,bid);
        assertNotNull(fbIgCampaign);
        fbIgCampaign.waitForPageLoaded(driver);

        // Verify success toast
        String bidString = "";
        String[] bidSArray = String.format("%.2f",bid).split("\\.");
        if (bidSArray.length == 2) {
            bidString = bidSArray[0] + bidSArray[1];
        }
        assertTrue(fbIgCampaign.getAlertMessage(driver).contains("Successfully updated bid amount to " + bidString),"Fail to verify success toast for bulk bid update.");

        // Close alert box
        fbIgCampaign.closeAlert(driver);

        // Retrieve list of rows of adset and verify budget in each row updated to the correct value
        List<HashMap<String,String>> adSets = fbIgCampaign.getAdSetsList(driver);
        assertNotNull(adSets);
        assertFalse(adSets.isEmpty(),"Ad Sets is empty for campaign" + cpName + "of initiative with id " + initID);

        for (HashMap<String,String> ad: adSets) {
            String budgetDisplay = ad.get("Max Bids").split("\n")[0];
            assertEquals(budgetDisplay,String.format("$%.2f",bid));
        }
    }

    @Test(enabled = true,dataProvider = "getCampaignEndDate")
    public void bulk_Update_EndDate(String cpName,Integer initID,String platform,Integer endDateOffset,String endDateOffsetUnit,String endTime) {
        LOG.info("Starting bulk_Update_EndDate......");

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

        switch(platform) {
            case "Facebook":
            case "Instagram":
                // Go to specific campaign for bulk update
                FbIgBase fbIgCampaign = initPage.goToCampaign(driver,cpName);
                fbIgCampaign.waitForPageLoaded(driver);

                // Perform bulk end date
                fbIgCampaign = fbIgCampaign.bulkEndDateUpdate(driver,cpName,endDate,endTime);
                fbIgCampaign.waitForPageLoaded(driver);

                // Verify success toast
                assertTrue(fbIgCampaign.getAlertMessage(driver).contains("Successfully updated end time to "),"Fail to verify success toast");

                // Close alert box
                fbIgCampaign.closeAlert(driver);

                // Retrieve list of rows of adset and verify budget in each row updated to the correct value
                List<HashMap<String,String>> adSets = fbIgCampaign.getAdSetsList(driver);
                assertNotNull(adSets);
                assertFalse(adSets.isEmpty(),"Ad Sets is empty for campaign" + cpName + "of initiative with id " + initID);

                for (HashMap<String,String> ad: adSets) {
                    assertTrue(ad.get("End Date").contains(CommonUtil.getDateStringByFormat(endDate,"M/d/yy") + "\n" + endTime));
                }
                break;
            case "Pinterest":
                // Go to specific campaign for bulk update
                PinterestCampaign pinCampaign = initPage.goToPinterestCampaign(driver,cpName);
                pinCampaign.waitForPageLoaded(driver);

                // Perform bulk end date
                pinCampaign = pinCampaign.bulkEndDateUpdate(driver,cpName,endDate,"");
                pinCampaign.waitForPageLoaded(driver);

                // Verify success toast
                assertTrue(pinCampaign.getAlertMessage(driver).contains("Successfully updated end time to "),"Fail to verify success toast");

                // Close alert box
                pinCampaign.closeAlert(driver);

                // Retrieve list of rows of adset and verify budget in each row updated to the correct value
                List<HashMap<String,String>> ads = pinCampaign.getAdsList(driver);
                assertNotNull(ads);
                assertFalse(ads.isEmpty(),"Ad Sets is empty for campaign" + cpName + "of initiative with id " + initID);

                for (HashMap<String,String> ad: ads) {
                    assertTrue(ad.get("End Date").contains(CommonUtil.getDateStringByFormat(endDate,"M/d/yy")));
                }
                break;
            default:
                fail("Invalid value for platform. Expect to be one of the following: Facebook, Instagram, Pinterest or Twitter.");
        }

    }
}
