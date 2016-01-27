package com.socialcode.webdriver.pages.campaigns;

import com.socialcode.webdriver.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by anthonyc on 1/24/16.
 */
public class CampaignsListPage extends BasePage {
    private static Logger LOG = LoggerFactory.getLogger(CampaignsListPage.class);

    WebDriver driver;

    @FindBy(id = "campaign-table")
    protected WebElement campaignTable;

    @FindBy(id = "campaign-search")
    protected WebElement campaignSearchEdit;

    public CampaignsListPage(WebDriver aDriver) {
        driver = aDriver;

        pageInitElements(driver,this);

        if (!isPageLoaded()) {
            assert false : "This is not Campaign List Page";
        }

        LOG.info("VERIFIED - Campaign List Page is loaded");
    }

    /**
     *  Checks if Campaign List Page is loaded
     * @return true if page is loaded; false otherwise
     */
    public boolean isPageLoaded() {
        LOG.debug("Verifying Campaign List Page is loaded");
        if (waitForElementVisible(driver,campaignTable)) {
            return true;
        }
        return false;
    }

    /**
     * Search campaign by name using input value provided by cpName
     * @param aDriver
     * @param cpName
     * @return Campaign page for searched campaign if found; null otherwise
     */
    public CampaignPage searchCampaignByName(WebDriver aDriver,String cpName) {
        waitForPageLoaded(aDriver);
        campaignSearchEdit.sendKeys(cpName + Keys.ENTER);
        if (waitForElementPresence(aDriver,"//a[text() = '"+cpName+"']")) {
            waitForPageLoaded(aDriver);
            WebElement element = aDriver.findElement(By.xpath("//a[text() = '"+cpName+"']"));
            aDriver.get(element.getAttribute("href"));
            return (new CampaignPage(aDriver,cpName));
        }
        return null;
    }
}
