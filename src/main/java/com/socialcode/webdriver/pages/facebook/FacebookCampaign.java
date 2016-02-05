package com.socialcode.webdriver.pages.facebook;

import com.socialcode.webdriver.pages.campaigns.FbIgBase;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by anthonyc on 1/25/16.
 */
public class FacebookCampaign extends FbIgBase {
    private static Logger LOG = LoggerFactory.getLogger(FacebookCampaign.class);

    @FindBy(xpath = "//*[@class = 'create-ad-nav']/i")
    protected WebElement fbIcon;

    public FacebookCampaign(WebDriver aDriver,String cpName) {
        super(aDriver,cpName);

        pageInitElements(aDriver,this);

        if (!isPageLoaded()) {
            assert false : "This is not Facebook Campaign Page";
        }

        LOG.info("VERIFIED - Facebook Campaign Page is loaded");
    }

    /**
     * Checks if Facebook Campaign Page is loaded
     * @return true if page is loaded with correct platform icon; false otherwise
     */
    public boolean isPageLoaded() {
        LOG.debug("Verifying Facebook Campaign Page is loaded");
        if (waitForElementVisible(driver,fbIcon)) {
            return fbIcon.getAttribute("class").contains("facebook");
        }
        return false;
    }
}
