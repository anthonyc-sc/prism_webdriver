package com.socialcode.webdriver.tests.initiatives;

import com.socialcode.webdriver.pages.initiatives.InitiativePage;
import com.socialcode.webdriver.pages.initiatives.InitiativesListPage;
import com.socialcode.webdriver.pages.login.LoginPage;
import com.socialcode.webdriver.tests.CommonUtil;
import com.socialcode.webdriver.tests.TestData;
import com.socialcode.webdriver.tests.WebDriverSetup;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by anthonyc on 1/22/16.
 */
public class DeleteInitiative extends WebDriverSetup {
    private static Logger LOG = LoggerFactory.getLogger(DeleteInitiative.class);

    public DeleteInitiative() {
        data = new TestData();
        data.load(dataFolder + "initiativedatafordeletion.xml");
    }

    @DataProvider(name = "getInitiatives")
    public Object[][] getInitiatives() {
        String[] cols = {"name","corporation","brand","start","duration","unit","account_data"};
        return data.getDataByElement("initiative",cols);
    }

    @DataProvider(name ="getDInit")
    public Object[][] getInitToDelete(){
        String[] cols = {"name"};
        return data.getDataByElement("initiative",cols);
    }

    @Test(dataProvider = "getInitiatives")
    public void createInitiativeForDeletion(String initName,String corporation,String brand,String startDateFlag,Integer duration,String unit,String acctData) throws Exception {
        LOG.info("Starting createInitiativeForDeletion.....");

        String startDate = CommonUtil.getDateByDuration(startDateFlag,0);
        String endDate = CommonUtil.getDateByDuration(unit,duration);

        assertFalse(startDate.isEmpty(),"Can't compute start_date");
        assertFalse(endDate.isEmpty(),"Can't compute end_date");

        // Navigate to Advisor-V2 application login screen
        driver.get(prismURL);

        // Log in with default username and password and verify initiative list page is displayed
        InitiativesListPage initListPage = (new LoginPage(driver)).enterLoginId(loginID).enterPassword(password).submit();
        assertNotNull(initListPage,"Fail to login to Prism.");

        // Create new initiative
        InitiativePage initPage = initListPage.createInitiativeWithAccount(driver,initName,corporation,brand,startDate,endDate,acctData);
        assertNotNull(initPage,"Fail to create new initiative.");
    }

    @Test(dependsOnMethods = { "createInitiativeForDeletion" },dataProvider = "getDInit")
    public void TC1_3_Delete_an_Initiative(String initName) throws Exception {
        LOG.info("Starting TC1_3_Delete_an_Initiative.....");

        // Navigate to Advisor-V2 application login screen
        driver.get(prismURL);

        // Log in with default username and password and verify initiative list page is displayed
        InitiativesListPage initListPage = (new LoginPage(driver)).enterLoginId(loginID).enterPassword(password).submit();
        assertNotNull(initListPage,"Fail to login to Prism.");
        initListPage.waitForPageLoaded(driver);
        initListPage.waitForAjax(driver);

        // Search for initiative with specified name
        assertTrue(initListPage.searchInitiative(driver,initName),"Not able to find initiative " + initName);

        // Go to specified initiative
        InitiativePage initPage = initListPage.goToInitiativePageByName(driver,initName);

        // Delete specified initiative
        initListPage = initPage.deleteInitiative(driver);

        // Wait for operation alert message
        String alertMsg = initListPage.getAlertMessage(driver);
        assertEquals(alertMsg,"Successfully deleted initiative " + initName,
                "Expect message: Successfully deleted initiative " + initName + ",but get: " + alertMsg);

        // Close the alert message dialog
        initListPage.waitForPageLoaded(driver);
        initListPage.waitForAjax(driver);
        initListPage.closeAlert(driver);

        // Search for deleted initiative and verify it's no longer present
        String msg = initListPage.searchInitiativeExpectNoResult(driver,initName);
        assertEquals(msg,"No results found.");
    }
}
