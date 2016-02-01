package com.socialcode.webdriver.tests;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by anthonyc on 12/9/15.
 */
public class CommonUtil {

    private static final Integer daysPerMonth = 30;
    private static final Integer daysPerYear = 365;

    /**
     * Method to get date in specified format
     * @param offset
     * @param format
     * @return string containing requested date in specified format
     */
    public static String getDate(Integer offset,String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, offset);
        return sdf.format(c.getTime());
    }

    public static String getDateStringByFormat(String input,String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String newDateString = "";
        try {
            newDateString = sdf.format(sdf.parse(input));
        } catch (Exception e) {

        }
        return newDateString;
    }

    /**
     * Method to convert given date into specific format
     * @param input
     * @return string containing date in converted format
     */
    public static String convertDateString(String input) {
        String convertedDate = "";
        String[]  dElements = input.split("/");
        if (dElements.length != 3) {
            return convertedDate;
        }

        SimpleDateFormat ft = new SimpleDateFormat("MM/dd/yyyy");
        Date t;
        String dayTag = "th";
        try {
            int day = Integer.parseInt(dElements[1]);
            switch (day) {
                case 1:
                case 21:
                case 31: dayTag = "st";
                    break;
                case 2:
                case 22:
                    dayTag = "nd";
                    break;
                case 3:
                case 23: dayTag = "rd";
                    break;
                default: break;
            }
            t = ft.parse(input);
            convertedDate = String.format("%tb %te%s %tY",t,t,dayTag,t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertedDate;
    }

    /**
     * Retrieves date string with specified duration
     * @param flag
     * @param duration
     * @return string containing requested date
     */
    public static String getDateByDuration(String flag,Integer duration) {
        String nDate = "";
        switch(flag) {
            case "current_date": nDate = CommonUtil.getDate(duration,"MM/dd/yyyy");
                                 break;
            case "day": nDate = CommonUtil.getDate(duration, "MM/dd/yyyy");
                        break;
            case "month": nDate = CommonUtil.getDate(duration*CommonUtil.daysPerMonth,"MM/dd/yyyy");
                        break;
            case "year": nDate = CommonUtil.getDate(duration*CommonUtil.daysPerYear,"MM/dd/yyyy");
                         break;
            default:
        }
        return nDate;
    }
}
