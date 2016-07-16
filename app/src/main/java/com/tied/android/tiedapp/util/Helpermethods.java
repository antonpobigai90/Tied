package com.tied.android.tiedapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Emmanuel on 7/3/2016.
 */
public class HelperMethods {

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

    public static int getDayOfTheWeek(String day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(day);
            Calendar c = Calendar.getInstance();
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            c.setTime(date);
            return dayOfWeek;
        } catch (ParseException e) {
            return 0;
        }
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
}