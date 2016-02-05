package com.socialcode.webdriver.pages.instagram;

import com.socialcode.webdriver.pages.campaigns.FbIgBase;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by anthonyc on 2/4/16.
 */
public class InstagramCampaign extends FbIgBase {
    private static Logger LOG = LoggerFactory.getLogger(InstagramCampaign.class);

    @FindBy(xpath = "//*[@class = 'create-ad-nav']/i")
    protected WebElement instaIcon;


    public InstagramCampaign(WebDriver aDriver, String cpName) {
        super(aDriver,cpName);

        pageInitElements(aDriver,this);

        if (!isPageLoaded()) {
            assert false : "This is not Instagram Campaign Page";
        }

        LOG.info("VERIFIED - Instagram Campaign Page is loaded");
    }

    /**
     * Checks if Instagram Campaign Page is loaded
     * @return true if page is loaded with correct platform icon; false otherwise
     */
    public boolean isPageLoaded() {
        LOG.debug("Verifying Instagram Campaign Page is loaded");
        if (waitForElementVisible(driver,instaIcon)) {
            return instaIcon.getAttribute("class").contains("instagram");
        }
        return false;
    }

}
