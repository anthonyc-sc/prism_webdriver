package com.socialcode.webdriver.tests.listeners;

import java.io.PrintStream;
import java.util.*;

import jxl.format.*;
import jxl.format.Colour;
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
    private int startRow = 3;
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
        for (ISuite suite : suites) {
            // Get suite name
            String suiteName = suite.getName();

            //  Getting the results for this test suite
            Map<String,ISuiteResult> suiteResults = suite.getResults();

            for (ISuiteResult sr : suiteResults.values()) {
                // Get Test object in each test suite result object
                ITestContext tc = sr.getTestContext();

                // Per test specified in TestNG xml, get the list of Failed , Passed and Skipped test result
                IResultMap failedTests = tc.getFailedTests();
                IResultMap passedTests = tc.getPassedTests();
                IResultMap skippedTests = tc.getSkippedTests();

                // get individual test result in the list.
                getTestStatus(failedTests,testResults);
                getTestStatus(passedTests,testResults);
                getTestStatus(skippedTests,testResults);

            }
        }
        writeTestReportToExcel(testResults);
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

            for (int j = 0; j < parameter.length; j++) {
                if (parameter[j].toString().startsWith(testCaseDelimeter)) {
                    result.put("Test Case",parameter[j].toString());
                }
            }
            if (parameter.length == 0) {
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
            testResults.add(result);
        }
    }

    public void writeTestReportToExcel(List<HashMap<String,Object>> testResults) {
        try {
            File f = new File(testResultTemplate);
            if (!f.exists()) {
                System.out.println(testResultTemplate + " file Not Exists!");
            }

            Workbook rWorkbook = Workbook.getWorkbook(f);
            WritableWorkbook workbook = Workbook.createWorkbook(new File(testResultOutput), rWorkbook);
            WritableSheet sheet = workbook.getSheet(0);

            WritableFont times12font = new WritableFont(WritableFont.TIMES, 12);
            WritableCellFormat format = new WritableCellFormat (times12font);

            for (HashMap<String, Object> tr : testResults) {
                if (tr.containsKey("Test Case")) {
                    Label label = new Label(testCaseColumn,startRow,tr.get("Test Case").toString(),format);
                    sheet.addCell(label);

                    if (tr.containsKey("Result")) {
                        WritableCellFormat fmt = format;
                        if (tr.get("Result").toString().contentEquals("Failed")) {
                            WritableFont times12Font = new WritableFont(WritableFont.TIMES,12,WritableFont.BOLD,false, UnderlineStyle.NO_UNDERLINE, Colour.RED);
                            WritableCellFormat times12format = new WritableCellFormat (times12Font);
                            fmt = times12format;
                        }
                        Label labelR = new Label(testResultColumn,startRow,tr.get("Result").toString(),fmt);
                        sheet.addCell(labelR);
                    }

                    if (tr.containsKey("Method Call")) {
                        Label labelM = new Label(testMethodColumn,startRow,tr.get("Method Call").toString(),format);
                        sheet.addCell(labelM);
                    }
                    if (tr.containsKey("Error")) {
                        WritableHyperlink lnk =  new WritableHyperlink(testErrorColumn,startRow,new File(tr.get("Error").toString()),"Click Here To See Error Detail");
                        sheet.addHyperlink(lnk);
                    }
                    startRow++;
                }
            }

            workbook.write();
            workbook.close();
            rWorkbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
