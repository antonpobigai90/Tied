package com.tied.android.tiedapp.util;

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
}