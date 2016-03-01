package com.socialcode.webdriver.tests.listeners;

import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import com.socialcode.webdriver.tests.WebDriverSetup;
import jxl.format.*;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.*;
import org.testng.xml.XmlSuite;
import java.io.File;
import jxl.*;
import jxl.write.*;

/**
 * Created by anthonyc on 2/19/16.
 */
public class CustomReportListener implements IReporter {
    private int testCaseColumn = 0;
    private int testResultColumn = 1;
    private int testMethodColumn = 2;
    private int testErrorColumn = 3;
    private int screenShotColumn = 4;
    private int startRow = 4;
    private String testCaseDelimeter = "TC";
    private String testResultTemplate = System.getProperty("user.dir") + System.getProperty("file.separator") + ".." +
            System.getProperty("file.separator") + ".." +
            System.getProperty("file.separator") + ".." +
            System.getProperty("file.separator") + "src" +
            System.getProperty("file.separator") + "test" +
            System.getProperty("file.separator") + "resources" +
            System.getProperty("file.separator") + "smokeTestResultTemplate.xls";

    private String testResultOutput = "TestResult.xls";


    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,String outputDirectory) {
        List<HashMap<String,Object>> testResults = new ArrayList<HashMap<String,Object>>();

        // Iterating over each suite included in the TestNG xml file
        String browser = "";
        for (ISuite suite : suites) {
            // Get suite name
            String suiteName = suite.getName();

            //  Getting the results for this test suite
            Map<String,ISuiteResult> suiteResults = suite.getResults();

            WebDriver aDriver = null;
            for (ISuiteResult sr : suiteResults.values()) {
                // Get Test object in each test suite result object
                ITestContext tc = sr.getTestContext();

                if (tc.getAllTestMethods().length > 0) {
                    WebDriverSetup wDriver = (WebDriverSetup)(tc.getAllTestMethods()[0].getInstance());
                    aDriver = wDriver.getDriver();
                    browser = getBrowserAndVersion(aDriver);
                }
                // Per test specified in TestNG xml, get the list of Failed , Passed and Skipped test result
                IResultMap failedTests = tc.getFailedTests();
                IResultMap skippedTests = tc.getSkippedTests();
                IResultMap passedTests = tc.getPassedTests();

                // get individual test result in the list.
                getTestStatus(failedTests,testResults);
                getTestStatus(skippedTests,testResults);
                getTestStatus(passedTests,testResults);

            }
        }
        writeTestReportToExcel(testResults,browser);
    }


    public void getTestStatus(IResultMap iRMap,List<HashMap<String,Object>> testResults)  {
        Object[] sResultSet = iRMap.getAllResults().toArray();

        for (int i = 0; i < sResultSet.length; i++) {
            HashMap<String,Object> result = new HashMap<String,Object>();

            Object[] parameter = ((ITestResult)sResultSet[i]).getParameters();
            String methodName = ((ITestResult)sResultSet[i]).getMethod().getMethodName();
            String mCall = methodName + "(";
            for (int k = 0; k < parameter.length; k++) {
                if (k == parameter.length - 1) {
                    mCall = mCall + parameter[k];
                } else {
                    mCall = mCall + parameter[k] + ",";
                }
            }
            result.put("Method Call",mCall + ")");

            boolean foundTestParam = false;
            for (int j = 0; j < parameter.length; j++) {
                if (parameter[j].toString().startsWith(testCaseDelimeter)) {
                    result.put("Test Case",parameter[j].toString());
                    foundTestParam = true;
                    break;
                }
            }
            if ((parameter.length == 0) || (!foundTestParam)) {
                result.put("Test Case",((ITestResult)sResultSet[i]).getMethod().getDescription());
            }

            switch (((ITestResult)sResultSet[i]).getStatus()) {
                case ITestResult.FAILURE:
                    result.put("Result","Failed");
                    break;
                case ITestResult.SKIP:
                    result.put("Result","Skipped");
                    break;
                case ITestResult.SUCCESS:
                    result.put("Result","Passed");
                    break;
                case ITestResult.STARTED:
                    result.put("Result","Started");
                    break;
            }

            Throwable th = ((ITestResult)sResultSet[i]).getThrowable();
            if (th != null) {
                try {
                    String ErrorFileName = methodName + "_" + System.currentTimeMillis() + ".txt";
                    th.printStackTrace(new PrintStream(new File(ErrorFileName)));
                    result.put("Error",System.getProperty("user.dir") + System.getProperty("file.separator") + ErrorFileName);
                } catch (Exception e) {
                    System.out.println("Error Stream");
                }
            }

            if (((ITestResult)sResultSet[i]).getAttribute("Screen Shot") != null) {
                result.put("Screen Shot",((ITestResult)sResultSet[i]).getAttribute("Screen Shot"));
            }
            testResults.add(result);
        }
    }

    public void writeTestReportToExcel(List<HashMap<String,Object>> testResults,String browser) {
        try {
            File f = new File(testResultTemplate);
            if (!f.exists()) {
                System.out.println(testResultTemplate + " file Not Exists!");
                return;
            }

            Workbook rWorkbook = Workbook.getWorkbook(f);
            WritableWorkbook workbook = Workbook.createWorkbook(new File(testResultOutput), rWorkbook);
            WritableSheet sheet = workbook.getSheet(0);

            WritableFont times14font = new WritableFont(WritableFont.TIMES, 14,WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.BLACK);
            WritableCellFormat format14 = new WritableCellFormat (times14font);
            format14.setBorder(Border.ALL, BorderLineStyle.THIN,Colour.BLACK);

            String build = System.getenv("BUILD_NUMBER");
            Date date = new Date();
            Label label = new Label(0,2,"Build #" + build + " Ran On: " + date.toString() + ", Browser: " + browser,format14);
            sheet.addCell(label);

            WritableFont times12font = new WritableFont(WritableFont.TIMES, 12, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
            WritableCellFormat format = new WritableCellFormat (times12font);
            format.setBackground(Colour.LIGHT_GREEN);
            format.setBorder(Border.ALL,BorderLineStyle.THIN,Colour.BLACK);

            for (HashMap<String, Object> tr : testResults) {
                WritableCellFormat fmt = format;

                if (tr.containsKey("Result")) {
                    if (tr.get("Result").toString().contentEquals("Failed")) {
                        WritableFont times12Font = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.RED);
                        WritableCellFormat times12format = new WritableCellFormat(times12Font);
                        times12format.setBackground(Colour.VERY_LIGHT_YELLOW);
                        times12format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
                        fmt = times12format;
                    } else if (tr.get("Result").toString().contentEquals("Skipped")) {
                        WritableFont times12Font = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.DARK_BLUE);
                        WritableCellFormat times12format = new WritableCellFormat(times12Font);
                        times12format.setBackground(Colour.IVORY);
                        times12format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
                        fmt = times12format;
                    }
                }

                if (tr.containsKey("Test Case") && (tr.get("Test Case") != null)) {
                    Label labelT = new Label(testCaseColumn, startRow, tr.get("Test Case").toString(), fmt);
                    sheet.addCell(labelT);
                }

                if (tr.containsKey("Result") && (tr.get("Result") != null)) {
                    Label labelR = new Label(testResultColumn,startRow,tr.get("Result").toString(),fmt);
                    sheet.addCell(labelR);
                }

                if (tr.containsKey("Method Call") && (tr.get("Method Call") != null)) {
                    Label labelM = new Label(testMethodColumn,startRow,tr.get("Method Call").toString(),fmt);
                    sheet.addCell(labelM);
                }

                Label labelE = new Label(testErrorColumn,startRow,"",fmt);
                sheet.addCell(labelE);
                if (tr.containsKey("Error") && (tr.get("Error") != null)) {
                    WritableHyperlink lnk =  new WritableHyperlink(testErrorColumn,startRow,new File(tr.get("Error").toString()),"Click Here To See Error Detail");
                    sheet.addHyperlink(lnk);
                }

                Label labelF = new Label(screenShotColumn,startRow,"",fmt);
                sheet.addCell(labelF);
                if (tr.containsKey("Screen Shot") && (tr.get("Screen Shot") != null)) {
                    Path p = Paths.get(tr.get("Screen Shot").toString());
                    WritableHyperlink lnkS = new WritableHyperlink(screenShotColumn,startRow,p.toFile(),"Click Here To See Screen Shot");
                    sheet.addHyperlink(lnkS);
                }
                startRow++;
            }

            workbook.write();
            workbook.close();
            rWorkbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getBrowserAndVersion(WebDriver browserDriver) {
        String browser_version = null;
        Capabilities cap = ((RemoteWebDriver) browserDriver).getCapabilities();
        String browsername = cap.getBrowserName().substring(0,1).toUpperCase() +  cap.getBrowserName().substring(1);

        // This block to find out IE Version number
        if ("internet explorer".equalsIgnoreCase(browsername)) {
            String uAgent = (String) ((JavascriptExecutor) browserDriver).executeScript("return navigator.userAgent;");
            System.out.println(uAgent);
            //uAgent return as "MSIE 8.0 Windows" for IE8
            if (uAgent.contains("MSIE") && uAgent.contains("Windows")) {
                browser_version = uAgent.substring(uAgent.indexOf("MSIE")+5, uAgent.indexOf("Windows")-2);
            } else if (uAgent.contains("Trident/7.0")) {
                browser_version = "11.0";
            } else {
                browser_version = "0.0";
            }
        } else
        {
            //Browser version for Firefox and Chrome
            browser_version = cap.getVersion();// .split(".")[0];
        }
        String browserversion = browser_version.substring(0, browser_version.indexOf("."));
        return browsername + " version " + browserversion;
    }
}
