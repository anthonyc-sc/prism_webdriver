package com.socialcode.webdriver.tests;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

/**
 * Created by anthonyc on 12/9/15.
 */
public class TestData {
    protected Document oData;

    /**
     * Loads data file from specified file path
     * @param path
     */
    public void load(String path) {
        try {
            SAXReader reader = new SAXReader();
            oData = reader.read(this.getClass().getResourceAsStream(path));
        } catch (DocumentException e) {
            assert false : "Load test data : " + path + " failed!";
        }
    }

    /**
     * Parses input xml file to retrieve list of xml elements
     * @param dataSetName
     * @return list of elements
     */
    public List<Element> getElementList(String dataSetName) {
        List<Element> dataSetTemp = oData.getRootElement().elements();
        List<Element> dataSet = new ArrayList<Element>();
        for (Element elem: dataSetTemp) {
            if (elem.attributeValue("name").contentEquals(dataSetName)) {
                dataSet.add(elem);
            }
        }
        return dataSet;
    }

    /**
     * Parses input xml file and retreives specific data values and add them to double array object
     * @param dataSetName
     * @param cols
     * @return double array of object containing data values from input xml file
     */
    public Object[][] getDataByElement(String dataSetName,String[] cols) {
        List<Element> dataSet = getElementList(dataSetName);
        int setSize = dataSet.size();
        Object[][] dataArray = new Object[setSize][cols.length];

        for (int i = 0; i < setSize; i++) {
            Element data = dataSet.get(i);
            for (int j = 0; j < cols.length; j++) {
                if ((data.element(cols[j]).attributeValue("type") != null) && data.element(cols[j]).attributeValue("type").contentEquals("Integer")) {
                    try {
                        Integer n = Integer.parseInt(data.element(cols[j]).getStringValue());
                        dataArray[i][j] = n;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                } else if ((data.element(cols[j]).attributeValue("type") != null) && data.element(cols[j]).attributeValue("type").contentEquals("Double")) {
                    try {
                        Double d = Double.parseDouble(data.element(cols[j]).getStringValue());
                        dataArray[i][j] = d;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                } else {
                    dataArray[i][j] = data.element(cols[j]).getData();
                }
            }
        }
        return dataArray;
    }
}
