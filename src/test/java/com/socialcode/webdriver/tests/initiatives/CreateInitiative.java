package com.socialcode.webdriver.tests.initiatives;

import com.socialcode.webdriver.pages.initiatives.InitiativePage;
import com.socialcode.webdriver.pages.initiatives.InitiativesListPage;
import com.socialcode.webdriver.pages.login.LoginPage;
import com.socialcode.webdriver.tests.CommonUtil;
import com.socialcode.webdriver.tests.TestData;
import com.socialcode.webdriver.tests.WebDriverSetup;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Date;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by anthonyc on 12/1/15.
 */
public class CreateInitiative  extends WebDriverSetup {
    private static Logger LOG = LoggerFactory.getLogger(CreateInitiative.class);

    public CreateInitiative() {
        data = new TestData();
        data.load(dataFolder + "initiativedata_" + platF + ".xml");
    }

    @DataProvider(name = "getInitiatives")
    public Object[][] getInitiatives() {
        String[] cols = {"test","name","corporation","brand","start","duration","unit","status","account_data"};
        return data.getDataByElement("initiative",cols);
    }

    @Test(enabled = true,groups = {"initiative","create_initiative"},dataProvider = "getInitiatives")
    public void create_an_Initiative(String testCaseName,String initName,String corporation,String brand,String startDateFlag,Integer duration,String unit,String status,String acctData) throws Exception {
        LOG.info("Starting TC1_2_Create_an_Initiative.....");

        // Generate unique initiative name
        String initiativeName = initName + CommonUtil.getDate(0,"MMddyyhhmmss");
        String startDate = CommonUtil.getDateByDuration(startDateFlag,0);
        String endDate = CommonUtil.getDateByDuration(unit,duration);

        assertFalse(startDate.isEmpty(),"Can't compute start_date");
        assertFalse(endDate.isEmpty(),"Can't compute end_date");

        // Navigate to Prism application
        InitiativesListPage initListPage = LoginPage.launchApplicationPage(driver,prismURL,cookie);
        assertNotNull(initListPage,"Fail to login to Prism.");

      //  InitiativesListPage initListPage = (new LoginPage(driver)).enterLoginId(loginID).enterPassword(password).submit(driver);

        // Create new initiative
        InitiativePage initPage = initListPage.createInitiativeWithAccount(driver,initiativeName,corporation,brand,startDate,endDate,acctData);
        assertNotNull(initPage,"Fail to create new initiative.");

        // Verify initiative fields for the newly created initiative
        verifyNewInitiativeFields(initPage,initiativeName,startDate,endDate,status);
    }

    private void verifyNewInitiativeFields(InitiativePage initPage,String initiativeName,String startDate,String endDate,String status) {
        // Verify initiative fields for the newly created initiative
        assertEquals(initPage.getPageHeader(),initiativeName,"Expect initiative name: " + initiativeName + ",but get: " + initPage.getPageHeader());   // Verify campaign name
        assertEquals(initPage.getPacingValue(),"All campaigns on pace","Expect text 'All campaigns on pace' for Pacing,but get: " + initPage.getPacingValue());  // Verify Pacing text
        assertEquals(initPage.getMediaSpendValue(),"$0 0% of $0","Expect '$0 0% of $0',but get: " + initPage.getMediaSpendValue());   // Verify Media Spend value
        assertEquals(initPage.getInitStatus(),status,"Expect initiative status to be Inactive,but get: " + initPage.getInitStatus());  // Verify initiative status
        assertEquals(initPage.getInitBudget(),"$0.00","Expect initiative budget to be $0.0,but get: " + initPage.getInitBudget());

        String expectedInitDates = CommonUtil.convertDateString(startDate) + "—" + CommonUtil.convertDateString(endDate);
        assertEquals(initPage.getInitDateRange(),expectedInitDates,"Expect date range " + expectedInitDates + ",but get: " + initPage.getInitDateRange());  // Verify init date range
    }





}
