package com.tied.android.tiedapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Emmanuel on 7/3/2016.
 */
public class HelperMethods {
    protected static final String TAG = HelperMethods.class
            .getSimpleName();

    public static String[] MONTHS_LIST = {"January", "Febuary", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};

    public static String[] WEEK_LIST = {"", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday","Sunday"};

    // Converts to celcius
    public static float convertFahrenheitToCelcius(float fahrenheit) {
        return ((fahrenheit - 32) * 5 / 9);
    }

    // Converts to fahrenheit
    public static float convertCelciusToFahrenheit(float celsius) {
        return ((celsius * 9) / 5) + 32;
    }

    public static double kilometerToMile(double km){
        double miles;
        miles = km * 0.621371192;
        return miles;
    }

    public static String getDayOfTheWeek(String string_date) {
        GregorianCalendar gregorianCalendar = getGCalendar(string_date);
        int dayOfWeek=gregorianCalendar.get(gregorianCalendar.DAY_OF_WEEK);
        String dayOfWeekName= WEEK_LIST[dayOfWeek];
        return dayOfWeekName;
    }

    public static GregorianCalendar getGCalendar(String string_date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {

            Calendar c = Calendar.getInstance();
            Date date = sdf.parse(string_date);
            c.setTime(date);
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month, day -1);
            return gregorianCalendar;
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getMonthOfTheYear(String string_date) {
        GregorianCalendar gregorianCalendar = getGCalendar(string_date);
        int montOfyear = gregorianCalendar.get(gregorianCalendar.MONTH);
        String montOfyearName= MONTHS_LIST[montOfyear];
        return montOfyearName;
    }

    public static int getDayFromSchedule(String day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(day);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int month = cal.get(Calendar.DAY_OF_MONTH);
            return month;
        } catch (ParseException e) {
            return 0;
        }
    }

    public static long getDateDifference(String startDate, String endDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date s_date = sdf.parse(startDate);
            Date e_date = sdf.parse(endDate);
            //milliseconds
            long different = s_date.getTime() - e_date.getTime();

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;
            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            return elapsedDays;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long getDateDifferenceWithToday(String date){
        return getDateDifference(date, getTodayDate());
    }

    public static String getTodayDate(){
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String  today = sdf.format(now);
        return today;
    }
}