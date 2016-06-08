package com.tied.android.tiedapp.customs;

/**
 * Created by Emmanuel on 4/23/2016.
 */
public class Constants {

    public static final String SERVER_URL = "http://tied.goattale.com:8100/api/v1/";

    static final String TAG = "Tied App";

    public static final String HOST = "https://tied-api.herokuapp.com/";

    public static final int PORT = 3000;
    public static final String API_PATH = "api/v1/";

    public static final String AUTH_EMAIL_ENDPOINT = API_PATH + "auth/email";
    public static final String AUTH_REGISTER_ENDPOINT = API_PATH + "auth/register";
    public static final String AUTH_LOGIN_ENDPOINT = API_PATH + "auth/login";
    public static final String AUTH_SEND_PHONE_CODE_ENDPOINT = API_PATH + "auth/send_phone_vc";

    public static final String GET_AVATAR_ENDPOINT = HOST + "uploads/avatars/";

    public static final String USER_UPDATE_INFO = API_PATH + "users/me";

    public static final String GET_INDUSTRIES = API_PATH + "config/industries";

    public static final String USER = "user";
    public static final String CODE = "code";
    public static final String SERVER_INFO = "server_info";
    public static final String CURRENT_USER = "current_user";
    public static final String SPLASH_SCREEN_DONE = "splash_screen_done";
    public static final String LOGGED_OUT_USER = "logout";

    public static final String TOKEN = "token";

    public static final String CITY = "city";
    public static final String ZIP = "zip";
    public static final String STATE = "state";
    public static final String COUNTRY = "country";
    public static final String STREET = "street";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";

    public static final String PREVIOUS = "previous";

    public static final int Help = 0;
    public static final int SignInUser = 1;
    public static final int Reset = 2;
    public static final int DoneReset = 3;
    public static final int EmailSignUp = 4;
    public static final int Password = 5;
    public static final int Picture = 6;
    public static final int Name = 7;
    public static final int PhoneAndFax = 8;
    public static final int EnterCode = 9;
    public static final int OfficeAddress = 10;
    public static final int HomeAddress = 11;
    public static final int Territory = 12;
    public static final int SalesRep = 13;
    public static final int GroupDesc = 14;
    public static final int Industry = 15;
    public static final int AddBoss = 16;
    public static final int AddBossNow = 17;
    public static final int CoWorkerCount = 18;
    public static final int AddOptions = 19;
    public static final int CoWorker = 20;

    public static final int Completed = 25;

    public static final int CreateSchedule = 30;

    public static final String RECEIVER = "geo_address_receiver";
    public static final String FETCH_TYPE_EXTRA = "geo_address_fetch_extra";
    public static final int USE_ADDRESS_NAME = 1;
    public static final String LOCATION_NAME_DATA_EXTRA = "location_name_data_extra";
    public static final int FAILURE_RESULT = -1;
    public static final int SUCCESS_RESULT = 0;
    public static final String RESULT_ADDRESS = "address";
    public static final String RESULT_DATA_KEY = "data_key";




}
