package com.upwardproject.moviedb.util;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class LDate {

    public static String changeToIndonesianMonthName(int month) {
        switch (month) {
            case 1:
                return "Januari";
            case 2:
                return "Februari";
            case 3:
                return "Maret";
            case 4:
                return "April";
            case 5:
                return "Mei";
            case 6:
                return "Juni";
            case 7:
                return "Juli";
            case 8:
                return "Agustus";
            case 9:
                return "September";
            case 10:
                return "Oktober";
            case 11:
                return "November";
            case 12:
                return "Desember";
            default:
                return changeToIndonesianMonthName(Calendar.getInstance().get(Calendar.MONTH) + 1);
        }
    }

    public static String changeToMonthName(int month) {
        switch (month) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return changeToMonthName(Calendar.getInstance().get(Calendar.MONTH) + 1);
        }
    }

    public static String changeToDayName(int day) {
        switch (day) {
            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
            default:
                return null;
        }
    }

    public static String currentDate(String dateFormat) {
        Calendar cal = Calendar.getInstance();

        return getDateString(cal, dateFormat);
    }

    public static String currentDateFromMilliseconds(long milliseconds, String dateFormat) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milliseconds);

        return getDateString(cal, dateFormat);
    }

    private static String getDateString(Calendar cal, String dateFormat) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.getDefault());
        return format.format(cal.getTime());
    }

    public static String defaultTimeFormat(boolean includeSeconds) {
        return (includeSeconds) ? "HH:mm:ss" : "HH:mm";
    }

    public static String defaultDateFormat() {
        return "yyyy-MM-dd";
    }

    public static String defaultDateAndTimeFormat24Hour() {
        return "yyyy-MM-dd HH:mm";
    }

    public static String defaultDateAndTimeFormat24HourWithDay() {
        return "EEEE, yyyy-MM-dd HH:mm";
    }

    public static String defaultDateAndTimeFormat() {
        return "yyyy-MM-dd hh:mm";
    }

    public static String defaultDateAndTimeFormatWithDay() {
        return "EEEE, yyyy-MM-dd hh:mm";
    }

    public static String dateFormatddMMMyyyy() {
        return "dd MMM yyyy";
    }

    public static String dateFormatddMMMyyyyWithDay() {
        return "EEEE, dd MMM yyyy";
    }

    public static String dateAndTimeFormatddMMMyyyyWithDay() {
        return "EEEE, dd MMM yyyy HH:mm";
    }

    public static String digitFormat(int value) {
        if (value > 9) return String.valueOf(value);

        return "0" + value;
    }

    public static String getTime(String dates, String dateFormat, boolean includeSeconds) {
        Date date = getDateObject(dates, dateFormat);

        SimpleDateFormat timeFormat = new SimpleDateFormat(defaultTimeFormat(includeSeconds), Locale.getDefault());
        return timeFormat.format(date);
    }

    public static long getTimeAsLong(String dates, String dateFormat) {
        Date date = getDateObject(dates, dateFormat);

        return date.getTime();
    }

    public static Date getDateObject(String dates, String dateFormat) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.getDefault());

        try {
            date = format.parse(dates);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String getDetailedDate(String dates, String dateFormat, String outputFormat) {
        Date date = getDateObject(dates, dateFormat);

        SimpleDateFormat outputDateFormat = new SimpleDateFormat(outputFormat);
        return outputDateFormat.format(date);
    }

    public static String getDetailedDate(String dates, String dateFormat, boolean includeDayOfWeek) {
        Date date = getDateObject(dates, dateFormat);

        // Refer to http://developer.android.com/reference/java/text/SimpleDateFormat.html
        //String intMonth = (String) DateFormat.format("MM", date); //06
        String month = (String) DateFormat.format("MMMM", date); //Jan
        String year = (String) DateFormat.format("yyyy", date); //2013
        String day = (String) DateFormat.format("dd", date); //20
        String dayOfWeek = (String) DateFormat.format("EEEE", date); //Thursday

        // Use this to get Indonesian names
        /*
        Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		int dayOfWeekNumber = cal.get(Calendar.DAY_OF_WEEK); // Day of week index (Sunday is 1)
		String dayOfWeek = changeToDayName(dayOfWeekNumber);
		String month = changeToMonthName(Integer.parseInt(intMonth));
		
		return dayOfWeek + ", " + day + " " + month.substring(0, 3) + " " + year;
		*/

        String detailedDate = day + " " + month + " " + year;

        if (dateFormat.equals(defaultDateAndTimeFormat24Hour())) {
            String hour = (String) DateFormat.format("HH", date);
            String minute = (String) DateFormat.format("mm", date);

            return detailedDate + " " + hour + ":" + minute;
        }

        if (includeDayOfWeek) return dayOfWeek + ", " + detailedDate;

        return detailedDate;
    }

    public static boolean endTimeIsGreaterThanStartTime(String time1, String time2) {
        String[] times1 = time1.split(":");
        String[] times2 = time2.split(":");

        int seconds1 = (Integer.parseInt(times1[0]) * 3600) + ((Integer.parseInt(times1[1]) * 60));
        int seconds2 = (Integer.parseInt(times2[0]) * 3600) + ((Integer.parseInt(times2[1]) * 60));

        return seconds2 > seconds1;
    }

    public static boolean endDateisGreaterThanStartDate(String dates1, String dates2, String dateFormat) {
        Date date1 = getDateObject(dates1, dateFormat);
        Date date2 = getDateObject(dates2, dateFormat);

        long time1 = date1.getTime();
        long time2 = date2.getTime();

        return time2 > time1;
    }

    public static boolean endDateisEqualOrGreaterThanStartDate(String dates1, String dates2, String dateFormat) {
        Date date1 = getDateObject(dates1, dateFormat);
        Date date2 = getDateObject(dates2, dateFormat);

        long time1 = date1.getTime();
        long time2 = date2.getTime();

        return time2 >= time1;
    }

    // Get time range from today to a future date in day format
    public static long timeRangeFromTodayInDay(String endDate, String dateFormat) {
        return timeRangeInDay(currentDate(dateFormat), endDate, dateFormat);
    }

    public static long timeRangeInDay(String startDate, String endDate, String dateFormat) {
        Date d1 = getDateObject(startDate, dateFormat);
        Date d2 = getDateObject(endDate, dateFormat);

        //in milliseconds
        long diff = Math.abs(d2.getTime() - d1.getTime());
        return diff / (24 * 60 * 60 * 1000) % 7;
    }
}
