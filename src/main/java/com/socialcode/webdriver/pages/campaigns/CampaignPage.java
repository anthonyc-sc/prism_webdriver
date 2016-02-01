package com.socialcode.webdriver.pages.campaigns;

import com.socialcode.webdriver.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by anthonyc on 1/7/16.
 */
public class CampaignPage extends BasePage {
    private static Logger LOG = LoggerFactory.getLogger(CampaignPage.class);

    WebDriver driver;

    @FindBy(xpath = "//*[@class = 'sc-card-details']/div/h2")
    protected WebElement campaignNameHeader;

    @FindBy(xpath = "//*[@id = 'header-form']/div/a")
    protected WebElement newButton;

    @FindBy(xpath = "//a[text() = 'Ads']")
    protected WebElement newAdds;

    @FindBy(xpath = "//*[@id = 'campaign-details']/div/button")
    protected WebElement dropDownButton;

    @FindBy(id = "delete-campaign")
    protected WebElement deleteCampaignLink;

    @FindBy(xpath = "//*[@id = 'alert-region']/div/div/div")
    protected WebElement alertMessages;

    @FindBy(xpath = "//*[@class ='toast-buttons']/a[2]")
    protected WebElement closeAlertLink;

    public CampaignPage(WebDriver aDriver,String scName) {
        driver = aDriver;

        pageInitElements(driver,this);

        if (!isPageLoaded(scName)) {
            assert false : "This is not Campaign Page";
        }

        LOG.info("VERIFIED - Campaign Page is loaded");
    }

    /**
     * Checks if Campaign Page is loaded
     * @param scName
     * @return true if page is loaded with correct campaign name as header; false otherwise
     */
    public boolean isPageLoaded(String scName) {
        LOG.debug("Verifying Campaign Page is loaded");
        waitForPageLoaded(driver);
        waitForAjax(driver);
        if (waitForElementVisible(driver,campaignNameHeader)) {
            String str = campaignNameHeader.getText();
            System.out.println(str);
            return campaignNameHeader.getText().contentEquals(scName);
        }
        return false;
    }

    /**
     * Clicks New > Ads link to bring up the 'New Ads' modal
     * @param aDriver
     * @throws Exception
     */
    public void launchCreateNewAdsWindow(WebDriver aDriver) throws Exception {
        if (isVisible(newButton)) {
            newButton.click();
        }
        if (waitForElementClickable(aDriver,newAdds)) {
            Thread.sleep(5000);
            newAdds.click();
        }
    }

    /**
     * Clicks drop down > Delete Campaign link on the Campaign Page
     * @param aDriver
     * @return
     */
    public DeleteCampaignModal deleteCampaign(WebDriver aDriver) {
        if (waitForElementVisible(aDriver,dropDownButton)) {
            dropDownButton.click();
            if (waitForElementVisible(aDriver,deleteCampaignLink)) {
                deleteCampaignLink.click();
                return (new DeleteCampaignModal(aDriver));
            }
        }
        return null;
    }

    /**
     * Retrieves alert message
     * @param aDriver
     * @return string containing alert message if it's visible;empty string otherwise
     */
    public String getAlertMessage(WebDriver aDriver) {
        if (waitForElementVisible(aDriver,alertMessages)) {
            return alertMessages.getText();
        }
        return "";
    }

    /**
     * Closes the alert box
     * @param aDriver
     * @return true if successful;false otherwise
     */
    public Boolean closeAlert(WebDriver aDriver) {
        if (waitForElementVisible(aDriver,closeAlertLink)) {
            if (waitForElementClickable(aDriver,closeAlertLink)) {
                try {
                    closeAlertLink.click();
                    return true;
                } catch (WebDriverException e) {
                    // retry
                    try {
                        Thread.sleep(2000);
                        closeAlertLink.click();
                        return true;
                    } catch (WebDriverException ex) {
                        return false;
                    } catch (Exception ex2) {

                    }
                }
            }
        }
        return false;
    }

}
