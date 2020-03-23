package helpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

/**
 * This class provides functions for generic date
 * manipulation functions.
 */

public class FormatDates {
    private static String timeZone = "EST";

    /**
     * This method is used to format date.
     *
     * @param dateValue
     * @param inputFormat
     * @param outputFormat
     * @return
     * @throws ParseException
     */
    public String formatDate(String dateValue, String inputFormat,
                             String outputFormat) throws ParseException {
        SimpleDateFormat df1 = new SimpleDateFormat(inputFormat);

        SimpleDateFormat df2 = new SimpleDateFormat(outputFormat);
        Date inputDate = df1.parse(dateValue);

        return df2.format(inputDate);
    }

    /**
     * This method is used to get date time.
     *
     * @param dateformat
     * @return
     */
    public String getDateTime(String dateformat) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
        return sdf.format(cal.getTime());
    }

    /**
     * This method is used to get any date respect to current date.
     *
     * @param dateformat
     * @param day
     * @return
     */
    public String getAnyDateRespectToCurrentDate(String dateformat, int day) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
        cal.add(Calendar.DATE, day);
        return sdf.format(cal.getTime());
    }

    /**
     * This method is used to get est time.
     *
     * @param format
     * @return
     */
    public String getESTTime(String format) {
        SimpleDateFormat dateformat = new SimpleDateFormat(format);
        dateformat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        Calendar cal = Calendar.getInstance();
        return dateformat.format(cal.getTime());
    }

    /**
     * This method is used to get current date time.
     *
     * @return
     */
    public static String getCurrentDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("_yyyy-MM-dd_HH-mm-ss");
        Calendar cal = Calendar.getInstance();
        return "" + dateFormat.format(cal.getTime());
    }

    /**
     * This method is used to get current date time.
     *
     * @return
     */
    public static String getSysDate() {
        Date currentTime;
        SimpleDateFormat dateformat = new SimpleDateFormat("MMddhhmmSSSS");
        dateformat.setTimeZone(TimeZone.getTimeZone(timeZone));
        Calendar cal = Calendar.getInstance();
        currentTime = cal.getTime();
        return dateformat.format(currentTime) + new Random().nextInt(99);
    }

    /**
     * This method is used to get current date.
     *
     * @return
     */
    public static String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-YYYY");
        return sdf.format(date);
    }

    /**
     * This method is used to get current date and time.
     *
     * @return
     */
    public static String getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * This method is used to get current date.
     *
     * @param format
     * @return
     */
    public static String getCurrentDate(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * This method is used to get yesterday date.
     *
     * @return
     */
    public static String dateYesterday() {
        DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        Date date = yesterday.getTime();
        return sdf.format(date);
    }

    /**
     * This method is used to get current timestamp.
     *
     * @return
     */
    public static String getCurrentTimeStamp() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-YYYY-hh-mm-ss");
        return sdf.format(date);
    }
}
