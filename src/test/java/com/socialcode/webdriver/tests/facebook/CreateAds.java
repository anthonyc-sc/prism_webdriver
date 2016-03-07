package com.socialcode.webdriver.tests.facebook;

import com.socialcode.webdriver.pages.ads.NewAdsModal;
import com.socialcode.webdriver.pages.campaigns.CampaignPage;
import com.socialcode.webdriver.pages.initiatives.InitiativesListPage;
import com.socialcode.webdriver.pages.login.LoginPage;
import com.socialcode.webdriver.tests.WebDriverSetup;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.LocalFileDetector;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

/**
 * Created by anthonyc on 1/19/16.
 */
public class CreateAds extends WebDriverSetup {
    public CreateAds() {

    }

    @Test(enabled = false)
    public void createFBAds() throws Exception {
        // Scratch patch code. Work in progress....

        driver.get(prismURL);

        // Log in with default username and password and verify initiative list page is displayed
        InitiativesListPage initListPage = LoginPage.launchApplicationPage(driver,prismURL,cookie);
        assertNotNull(initListPage,"Fail to login to Prism.");

        driver.get("https://staging.socialcodedev.com/advisor-v2/#initiative/341/campaign/710");

        //CampaignPage cpPage = new CampaignPage(driver,"Test");
        //cpPage.launchCreateNewAdsWindow(driver);

        //NewAdsModal nAdsModal = new NewAdsModal(driver);
        //nAdsModal.enterAdClusterName("Testing");
        //nAdsModal.clickStartBulkUploadLink(driver);

        //driver.setFileDetector(new LocalFileDetector());
        //WebElement element = driver.findElement(By.className("dz-hidden-input"));
        //String js = "arguments[0].style.height='auto'; arguments[0].style.visibility='visible';";
        //((JavascriptExecutor) driver).executeScript(js, element);

        //String path = System.getProperty("user.home") + System.getProperty("file.separator") + "csv" + System.getProperty("file.separator") + "fb_mai.csv";
        //element.sendKeys(path);
        //WebElement elementS = driver.findElement(By.xpath("//button[text()='Upload and create ads']"));

        //elementS.click();
    }
}
