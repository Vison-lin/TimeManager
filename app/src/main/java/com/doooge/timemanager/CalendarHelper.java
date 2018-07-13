package com.doooge.timemanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CalendarHelper {

    /**
     * @param calendar input Calendar that needs to be converted to UTC format in String
     * @return return a String that represents that given calendar in UTC format
     * <p>
     * This method is used for converting Calendar Object to String in UTC format.
     */
    public static String convertCal2UTC(Calendar calendar) {
        TimeZone timeZone = calendar.getTimeZone();
        Date time = calendar.getTime();
        SimpleDateFormat outputFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);//SimpleDateFormat.getDateTimeInstance();
        outputFmt.setTimeZone(timeZone);//convert current calendar's time into standard GMT + 00:00
        return outputFmt.format(time);
    }

    /**
     * @param utc input UTC in String that needs to be converted to Calendar Object
     * @return return a Calendar Object that represents that given UTC in String
     * <p>
     * This method is used for converting UTC in String to Calendar Object.
     */
    public static Calendar convertUTC2Cal(String utc) {
        SimpleDateFormat outputFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);//SimpleDateFormat.getDateTimeInstance();

        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(outputFmt.parse(utc));
            calendar.setTimeZone(TimeZone.getDefault());//adjust time based on system timeZone (from GMT + 00:00)
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }


    /**
     * Used for converting NumberPickerDialog's users' choice into real Minutes.
     * Note: Only used for converting NumberPickerDialog only!
     *
     * @param input the integer that represent a specific time moment.
     * @return return the corresponding time in int format
     */
    public static int correctMinutes(int input) {
        switch (input) {
            case 0:
                return -180;

            case 1:
                return -165;

            case 2:
                return -150;

            case 3:
                return -135;

            case 4:
                return -120;

            case 5:
                return -105;

            case 6:
                return -90;

            case 7:
                return -75;

            case 8:
                return -60;

            case 9:
                return -45;

            case 10:
                return -30;

            case 11:
                return -15;

            case 12:
                return 0;

            case 13:
                return 15;

            case 14:
                return 30;

            case 15:
                return 45;

            case 16:
                return 60;

            case 17:
                return 75;

            case 18:
                return 90;

            case 19:
                return 105;

            case 20:
                return 120;

            case 21:
                return 135;

            case 22:
                return 150;

            case 23:
                return 160;

            case 24:
                return 180;

            default:
                throw new IllegalAccessError();
        }

    }

    /**
     * Returns a long number represents the duration between the given start time Calendar instance and the given end time Calendar instance, In Millis.
     *
     * @param start Start time of a duration in Calendar format
     * @param end   End time of a duration in Calendar format
     * @return return a long value represents the duration between the start time and the end time, In Millis.
     */
    public static long durationOfStartAndEndTimeInMillis(Calendar start, Calendar end) {
        return end.getTimeInMillis() - start.getTimeInMillis();
    }

}
