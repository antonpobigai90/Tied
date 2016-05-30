package com.tied.android.tiedapp.customs;

/**
 * Created by Emmanuel on 4/23/2016.
 */
public class Constants {

    public static final String HOST = "https://tied-api.herokuapp.com/";

    public static final int PORT = 3000;
    public static final String API_PATH = "api/v1/";

    public static final String AUTH_EMAIL_ENDPOINT = API_PATH + "auth/email";
    public static final String AUTH_REGISTER_ENDPOINT = API_PATH + "auth/register";

    public static final String USER_UPDATE_AVATAR = API_PATH + "users/avatar";
    public static final String USER_UPDATE_INFO = API_PATH + "users/me";

    public static final String AUTH_SIGN_UP_ENDPOINT = HOST + "auth/register";

    public static final String AUTH_SIGN_IN_ENDPOINT = HOST + "auth/login";
    public static final String AUTH_USER_NAME_ENDPOINT = HOST + "auth/username";

    public static final String USER_UPDATE_PROFILE = HOST + API_PATH + "users/profile/%s";

    public static final String USER_IMAGE_AVATAR = HOST + API_PATH + "users/image/%s";

    public static final String USER_URI = "http://clubapi.herokuapp.com/users";
    public static final String CHECK_EMAIL_URI = "http://clubapi.herokuapp.com/verify/users/";
    public static final String LOGIN_URI = "http://clubapi.herokuapp.com/auth/login/";


    public static final String USER = "user";
    public static final String CURRENT_USER = "current_user";
    public static final String SPLASH_SCREEN_DONE = "splash_screen_done2";




    public static final int WelcomeScreen = 0;
    public static final int SignInUser = 1;
    public static final int Reset = 2;
    public static final int DoneReset = 3;
    public static final int EmailSignUp = 4;
    public static final int Password = 5;
    public static final int Picture = 6;
    public static final int Name = 7;
    public static final int PhoneAndFax = 8;
    public static final int OfficeAddress = 9;

}
