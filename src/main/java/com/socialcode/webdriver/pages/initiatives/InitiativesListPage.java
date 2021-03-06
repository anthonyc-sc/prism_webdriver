package com.socialcode.webdriver.pages.initiatives;


import com.socialcode.webdriver.pages.BasePage;
import com.socialcode.webdriver.pages.campaigns.CampaignsListPage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by anthonyc on 12/1/15.
 */
public class InitiativesListPage extends BasePage {
    private static Logger LOG = LoggerFactory.getLogger(InitiativesListPage.class);

    @FindBy(id = "initiative-toolbar-options")
    protected WebElement initToolbar;

    @FindBy(id = "create-initiative")
    protected WebElement createInitButton;

    @FindBy(xpath = "//*[@id=‘global-loader-region’]/div/div/div")
    protected WebElement spinner;

    @FindBy(id = "initiative-search")
    protected WebElement initiativeSearch;

    @FindBy(xpath = "//*[@id = 'initiative-search']/../span")
    protected WebElement searchIcon;

    @FindBy(xpath = "//*[@id = 'alert-region']/div/div/div")
    protected WebElement alertMessage;

    @FindBy(xpath = "//*[@class = 'toast-buttons']/a[2]")
    protected WebElement alertCloseLink;

    @FindBy(className = "initiative-card-empty-sc")
    protected WebElement emptyInitListText;

    @FindBy(xpath = "//*[@id = 'initiative-toolbar-options']/ul/li[3]/a[text() = 'campaigns']")
    protected WebElement campaignsTab;

    public InitiativesListPage(WebDriver d) {
        driver = d;

        pageInitElements(driver,this);

        if (!isPageLoaded(d)) {
            assert false : "This is not 'Initiatives List' page";
        }
        LOG.info("VERIFIED - 'Initiatives List' page is loaded");
    }

    /**
     * Checks if 'Initiatives List' page is loaded
     * @return true if page is loaded; false otherwise
     */
    public synchronized boolean isPageLoaded(WebDriver aDriver) {
        LOG.debug("Verifying 'Initiatives List' page is loaded");
        waitForPageLoaded(aDriver);
        return waitForElementVisible(aDriver,initToolbar) && waitForElementVisible(aDriver,createInitButton);
    }

    /**
     * Method to create new initiative with account added to the initiative
     * @param aDriver
     * @param initName
     * @param corp
     * @param brand
     * @param startDate
     * @param endDate
     * @param acctData
     * @return Initiative Page object if successful;null otherwise
     * @throws Exception
     */
    public InitiativePage createInitiativeWithAccount(WebDriver aDriver, String initName, String corp,String brand,String startDate,String endDate,String acctData) throws Exception {
        InitiativeEditPage initEditPage = createInitiative(aDriver,initName,corp,brand,startDate,endDate);
        if (initEditPage == null) {
            return null;
        }

        initEditPage = addAccountToInitiative(aDriver,initEditPage,acctData);

        if (initEditPage == null) {
            return null;
        }

        return (initEditPage.saveChanges(aDriver));
    }

    /**
     * Method to handle adding account(s) and asset(s) to initiative
     * @param aDriver
     * @param initEditPage
     * @param acctData
     * @return Initiative Edit Page object if successful;null otherwise
     */
    protected InitiativeEditPage addAccountToInitiative(WebDriver aDriver,InitiativeEditPage initEditPage,String acctData) {
        String[] accountData = acctData.split(",");
        if (accountData.length < 2) {
            return null;
        }

        AddAccountModal addAccModal = initEditPage.launchAddAccountModal(aDriver);

        switch (accountData[0]) {
            case "Facebook":
                // Select platform
                if (!addAccModal.selectPlatform(aDriver,accountData[0])) {
                    return null;
                }

                // Enter account
                if (!addAccModal.selectAccount(aDriver,accountData[1])) {
                    return null;
                }

                // Asset is optional for FB. Enter if provided.
                if (accountData.length == 3) {
                    // Enter asset
                    if (!addAccModal.selectAsset(aDriver,accountData[2])) {
                        return null;
                    }
                }

                break;
            case "Instagram":
                if (accountData.length < 4) {
                    return null;
                }

                // Data in order: platform, instagram_assets, fb_account, fb_assets
                // Select platform
                if (!addAccModal.selectPlatform(aDriver,accountData[0])) {
                    return null;
                }

                // Enter Instagram Asset
                if (!addAccModal.selectInstagramAsset(aDriver,accountData[1])) {
                    return null;
                }

                // Enter fb account
                if (!addAccModal.selectAccount(aDriver,accountData[2])) {
                    return null;
                }

                // Enter FB Asset
                if (!addAccModal.selectFBAssetForInstagram(aDriver,accountData[3])) {
                    return null;
                }

                break;
            case "Pinterest":
                if (accountData.length != 2) {
                    return null;
                }

                // Data in order: platform, account
                // Select platform
                if (!addAccModal.selectPlatform(aDriver,accountData[0])) {
                    return null;
                }

                // Enter pinterest account
                if (!addAccModal.selectAccount(aDriver,accountData[1])) {
                    return null;
                }
                break;
            case "Twitter":
                if (accountData.length != 2) {
                    return null;
                }

                // Data in order: platform, account
                // Select platform
                if (!addAccModal.selectPlatform(aDriver,accountData[0])) {
                    return null;
                }

                // Enter Twitter account
                if (!addAccModal.selectAccount(aDriver,accountData[1])) {
                    return null;
                }
                break;
            default: return null;
        }

        return (addAccModal.submitAddAccount(driver));
    }

    /**
     * Wrapper method to create initiative without adding account to initiative
     * @param aDriver
     * @param initName
     * @param corp
     * @param brand
     * @param startDate
     * @param endDate
     * @return Initiative Page if successful;null otherwise
     * @throws Exception
     */
    public InitiativePage createInitiativeWithoutAccount(WebDriver aDriver, String initName, String corp,String brand,String startDate,String endDate) throws Exception {
        InitiativeEditPage initEditPage = createInitiative(aDriver,initName,corp,brand,startDate,endDate);
        if (initEditPage == null) {
            return null;
        }
        return (initEditPage.saveChanges(aDriver));
    }

    /**
     * Method to handle inputs for creating initiative and submits
     * @param aDriver
     * @param initName
     * @param corp
     * @param brand
     * @param startDate
     * @param endDate
     * @return Initiative Edit Page object if successful; null otherwise
     * @throws Exception
     */
    public InitiativeEditPage createInitiative(WebDriver aDriver,String initName, String corp,String brand,String startDate,String endDate) throws Exception {
        NewInitiativeModal initModal = launchCreateNewInitiativeModal();
        if (initModal != null) {
            if ((!initModal.inputInitiativeName(initName))
                    || (!initModal.inputCorporationName(corp))
                    || (!initModal.inputBrandName(brand))
                    || (!initModal.inputStartDate(startDate))
                    || (!initModal.inputEndDate(endDate))) {
                return null;
            }
            InitiativeEditPage initEditPage = initModal.submit(aDriver,initName);
            waitForPageLoaded(aDriver);
            waitForAjax(aDriver);
            return (initEditPage);
        }
        return null;
    }

    /**
     * Launch Create New Initiative Modal
     * @return New Initiative Modal objet if successful;null otherwise
     * @throws Exception
     */
    public NewInitiativeModal launchCreateNewInitiativeModal() throws Exception {
        if (waitForElementClickable(driver,createInitButton)) {
            try {
                if (waitForElementClickable(driver,createInitButton)) {
                    createInitButton.click();
                    waitForPageLoaded(driver);
                    waitForAjax(driver);
                    return (new NewInitiativeModal(driver));
                }
            } catch (Exception e) {
                // Try again
                Thread.sleep(5000);
                createInitButton.click();
                waitForPageLoaded(driver);
                waitForAjax(driver);
                return (new NewInitiativeModal(driver));
            }
        }
        return null;
    }

    /**
     * Checks if spinner is active
     * @return true if active;false otherwise
     */
    public boolean isSpinnerActive() {
        return spinner.isEnabled();
    }

    /**
     * Navigates to specific initiative having provided initiative id
     * @param initID
     * @return Initiative Page object if successful;failure otherwise
     */
    public InitiativePage gotoInitiative(Integer initID) {
        String url = driver.getCurrentUrl();
        driver.get(url + "/" + initID.toString());
        return (new InitiativePage(driver));
    }

    /**
     * Search initiative with given initiative name
     * @param aDriver
     * @param initName
     * @return true if found;false otherwise
     */
    public boolean searchInitiative(WebDriver aDriver,String initName)  {
        waitForPageLoaded(aDriver);
        waitForAjax(aDriver);
        if (waitForElementVisible(aDriver,initiativeSearch)) {
            initiativeSearch.sendKeys(initName);
            searchIcon.click();
            waitForPageLoaded(aDriver);
            if (waitForElementPresence(aDriver,60,"//a[text() = '"+initName+"']")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Search given initiative name. Expects no result found
     * @param aDriver
     * @param initName
     * @return  string with no results found if successful;empty string otherwise
     */
    public String searchInitiativeExpectNoResult(WebDriver aDriver,String initName) {
        waitForPageLoaded(aDriver);
        waitForAjax(aDriver);
        if (waitForElementVisible(aDriver,initiativeSearch) && waitForElementClickable(aDriver,searchIcon)) {
            initiativeSearch.sendKeys(initName);
            searchIcon.click();
            if (waitForElementVisible(aDriver,emptyInitListText)) {
                return emptyInitListText.getText();
            }
        }
        return "";
    }

    /**
     * Navigates to specified initiative with given initiative name
     * @param aDriver
     * @param initName
     * @return Initiative Page object if successful;null otherwise
     */
    public InitiativePage goToInitiativePageByName(WebDriver aDriver,String initName) {
        waitForPageLoaded(aDriver);
        waitForAjax(aDriver);
        if (waitForElementPresence(aDriver,"//a[text() = '"+initName+"']")) {
            WebElement element = aDriver.findElement(By.xpath("//a[text() = '"+initName+"']"));
            aDriver.get(element.getAttribute("href"));
            return (new InitiativePage(aDriver,initName));
        }
        return null;
    }

    /**
     * Retrieves alert message text
     * @param aDriver
     * @return string containing alert message text if successful;empty string otherwise
     */
    public String getAlertMessage(WebDriver aDriver) {
        if (waitForElementVisible(aDriver,alertMessage)) {
            return alertMessage.getText();
        }
        return "";
    }

    /**
     * Retrieves text specifying initiative list is empty
     * @param aDriver
     * @return string containing empty list message;empty string otherwise
     */
    public String getEmptyInitListText(WebDriver aDriver) {
        if (waitForElementVisible(aDriver,emptyInitListText)) {
            return emptyInitListText.getText();
        }
        return "";
    }

    /**
     * Closes alert message box
     * @param aDriver
     */
    public Boolean closeAlert(WebDriver aDriver) {
        if (waitForElementVisible(aDriver,alertCloseLink)) {
            if (waitForElementClickable(aDriver,alertCloseLink)) {
                try {
                    alertCloseLink.click();
                    return true;
                } catch (WebDriverException e) {
                    // retry
                    try {
                        Thread.sleep(2000);
                        alertCloseLink.click();
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

    /**
     * Navigates to the Campaign tab
     * @param aDriver
     * @return Campaign List Page object if successful;failure otherwise
     */
    public CampaignsListPage goToCampaignTab(WebDriver aDriver) {
        waitForPageLoaded(aDriver);
        if (waitForElementClickable(aDriver,campaignsTab)) {
            campaignsTab.click();
            return (new CampaignsListPage(aDriver));
        }
        return null;
    }
}
