package com.doooge.timemanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by fredpan on 2018/1/20.
 */

public class CalendarHelper {

    /**
     * @param calendar input Calendar that needs to be converted to UTC format in String
     * @return return a String that represents that given calendar in UTC format
     * <p>
     * This method is used for converting Calendar Object to String in UTC format.
     */
    public static String convertCal2UTC(Calendar calendar) {
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));//convert Date into UTC format
        Date time = calendar.getTime();
        SimpleDateFormat outputFmt = new SimpleDateFormat("MMM dd, yyy h:mm a zz");
        return outputFmt.format(time);
    }

    /**
     * @param utc input UTC in String that needs to be converted to Calendar Object
     * @return return a Calendar Object that represents that given UTC in String
     * <p>
     * This method is used for converting UTC in String to Calendar Object.
     */
    public static Calendar convertUTC2Cal(String utc) {
        SimpleDateFormat outputFmt = new SimpleDateFormat("MMM dd, yyy h:mm a zz");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(outputFmt.parse(utc));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }


}
