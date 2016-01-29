package com.socialcode.webdriver.pages.initiatives;

import com.socialcode.webdriver.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by anthonyc on 12/2/15.
 */
public class NewInitiativeModal extends BasePage {
    private static Logger LOG = LoggerFactory.getLogger(NewInitiativeModal.class);

    WebDriver driver;

    @FindBy(id = "sc-modal-title")
    protected WebElement modalTitle = null;

    @FindBy(id = "name")
    protected WebElement InitNameEdit = null;

    @FindBy(id = "s2id_corporation")
    protected WebElement corporationBox = null;

    @FindBy(xpath = "//li[text()='Please enter 1 or more character']/../../div/input")
    protected WebElement corpSearch = null;

    @FindBy(id = "s2id_brand_id")
    protected WebElement brandBox = null;

    @FindBy(xpath = "//li[text()='Please enter 1 or more character']/../../div/input")
    protected WebElement brandSearch = null;

    @FindBy(id = "start_date")
    protected WebElement startDateEdit = null;

    @FindBy(id = "end_date")
    protected WebElement endDateEdit = null;

    @FindBy(id = "submit")
    protected WebElement submitButton = null;


    public NewInitiativeModal(WebDriver d) {
        driver = d;

        pageInitElements(driver,this);

        if (!isPageLoaded()) {
            assert false : "This is not New Initiative Modal";
        }
        LOG.info("VERIFIED - New Initiative Modal is loaded");

    }

    /**
     * Checks if 'New Initiative Modal is loaded
     * @return true if modal with correct title is displayed;false otherwise
     */
    public boolean isPageLoaded() {
        LOG.debug("Verifying New Initiative Modal is loaded");
        if (waitForElementVisible(driver,modalTitle)) {
            return modalTitle.getText().contains("New Initiative");
        }
        return false;
    }

    /**
     * Enters initiative name on the modal
     * @param name
     * @return true if element is visible and value can be entered;false otherwise
     */
    public boolean inputInitiativeName(String name) {
        if (isVisible(InitNameEdit)) {
            InitNameEdit.sendKeys(name);
            return true;
        }
        return false;
    }

    /**
     * Enters corporation name on modal
     * @param corporation
     * @return true if successful;false otherwise
     */
    public boolean inputCorporationName(String corporation) {
        corporationBox.click();
        if (waitForElementPresence(driver,"//li[text()='Please enter 1 or more character']/../../div/input")) {
            corpSearch.sendKeys(corporation);
            if (waitForElementPresence(driver,"//div[text()='"+corporation+"']")) {
                WebElement element = driver.findElement(By.xpath("//div[text()='"+corporation+"']"));
                if (isVisible(element)) {
                    element.click();
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Enters brand name on modal
     * @param brand
     * @return true if successful;false otherwise
     */
    public boolean inputBrandName(String brand) {
        brandBox.click();
        if (waitForElementPresence(driver,"//li[text()='Please enter 1 or more character']/../../div/input")) {
            brandSearch.sendKeys(brand);
            if (waitForElementPresence(driver,"//div[text()='"+brand+"']")) {
                WebElement element = driver.findElement(By.xpath("//div[text()='"+brand+"']"));
                if (isVisible(element)) {
                    element.click();
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Enters start date on the modal
     * @param startDate
     * @return true if successful;false otherwise
     */
    public boolean inputStartDate(String startDate) {
        if (isVisible(startDateEdit)) {
            startDateEdit.sendKeys(startDate);
            return true;
        }
        return false;
    }

    /**
     * Enters end date on the modal
     * @param endDate
     * @return true if successful;false otherwise
     */
    public boolean inputEndDate(String endDate) {
        if (isVisible(endDateEdit)) {
            endDateEdit.sendKeys(endDate);
            return true;
        }
        return false;
    }

    /**
     * Clicks submit button the modal
     * @param d
     * @param initName
     * @return Initiative Edit Page object if successful;failure otherwise
     */
    public InitiativeEditPage submit(WebDriver d,String initName) {
        submitButton.click();
        return (new InitiativeEditPage(d,initName));
    }
}
