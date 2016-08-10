package com.tied.android.tiedapp.customs;

import com.loopj.android.http.PersistentCookieStore;

import java.io.File;

/**
 * Created by Emmanuel on 4/23/2016.
 */
public class Constants {
    public static final String TAG = Constants.class
            .getSimpleName();

    public static final String SERVER_URL = "http://tied.goattale.com:8102/api/v1/";

    public static final String HOST = "http://tied.goattale.com:8102/";
//    public static final String HOST = "https://tied-api.herokuapp.com/";

    public static final int PORT = 3000;
    public static final String API_PATH = "api/v1/";

    public static final String AUTH_EMAIL_ENDPOINT = API_PATH + "auth/email";
    public static final String AUTH_REGISTER_ENDPOINT = API_PATH + "auth/register";
    public static final String AUTH_LOGIN_ENDPOINT = API_PATH + "auth/login";
    public static final String AUTH_SEND_PHONE_CODE_ENDPOINT = API_PATH + "auth/send_phone_vc";
    public static final String AUTH_VERIFY_PHONE_CODE_ENDPOINT = API_PATH + "auth/send_phone_vc";

    public static final String GET_AVATAR_ENDPOINT = HOST + "uploads/avatars/";
    public static final String GET_LOGO_ENDPOINT = HOST + "uploads/logos/";

    public static final String USER_UPDATE_INFO = API_PATH + "users/me";
    public static final String USER_CHANGE_PASSWORD = API_PATH + "users/changePassword";

    public static final String CLIENTS = API_PATH + "clients";
    public static final String LINES = API_PATH + "lines";
    public static final String USER_CLIENTS = API_PATH + "users/me/clients";
    public static final String USER_CLIENTS_COUNT = API_PATH + "users/me/clients_count";
    public static final String USER_GE0_CLIENTS = API_PATH + "users/me/clients/geo";


    public static final String GET_CLIENT_WITH_ID = API_PATH + "client/{client_id}";
    public static final String EDIT_CLIENT_WITH_ID = API_PATH + "client/{client_id}";
    public static final String UPDATE_SCHEDULE_WITH_ID = API_PATH + "schedules/{schedule_id}";
    public static final String DELETE_SCHEDULE_WITH_ID = API_PATH + "schedules/{schedule_id}";

    public static final String SCHEDULES = API_PATH + "schedules";
    public static final String USER_SCHEDULE = API_PATH + "users/me/schedules";
    public static final String USER_SCHEDULE_COUNT = API_PATH + "users/me/schedules_count";
    public static final String USER_GE0_SCHEDULE = API_PATH + "users/me/schedules/geo";
    public static final String USER_SCHEDULES_BY_DATE = API_PATH + "users/me/schedules/date";

    public static final String GET_INDUSTRIES = API_PATH + "config/industries";

    public static final String APP_DATA = "app_data";
    public static final String USER_DATA = "user_data";
    public static final String CLIENT_DATA = "client_data";
    public static final String SCHEDULE_DATA = "schedule_data";
    public static final String SCHEDULE_DATE_FILTER = "schedule_date_filter";
    public static final String CODE = "code";
    public static final String SERVER_INFO = "server_info";
    public static final String CURRENT_USER = "current_user";
    public static final String NEW_USER = "new_user";
    public static final String SPLASH_SCREEN_DONE = "sp lash_screen_done";
    public static final String LOGGED_OUT_USER = "logout";
    public static final String LOGGED_IN_USER = "login";
    public static final String AVATAR_STATE_SAVED = "avatar_state";
    public static final String EditingProfile = "editing_profile";
    public static final String DISTANCE_UNIT = "distance_unit";
    public static final String LAST_TIME_APP_RAN = "last_ran";

    public static final String TOKEN = "token";
    public static final String TOKEN_HEADER = "x-access-token";

    public static final String TWITTER_API_KEY = "6b2QdtehythOUVjBwEyHKeFGl";
    public static final String TWITTER_API_SECRET = "hyjXQqyRekDAcpeXb1nrb1zVwjmiovXaoTVJNuZy80D0MuNBiR";

    public static final String SELECTED_DATE = "selected_date";
    public static final String SHOW_SELECTED_DATE = "show_selected_date";
    public static final String SCHEDULE_DATA_FILTER_INDEX = "filter";
    public static final String CITY = "city";
    public static final String ZIP = "zip";
    public static final String STATE = "state";
    public static final String COUNTRY = "country";
    public static final String STREET = "street";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String EMAIL = "email";
    public static final String FAX = "fax";
    public static final String COMPANY_NAME = "company_name";

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
    public static final int ActivitySchedule = 31;
    public static final int HomeSchedule = 32;
    public static final int ClientAdd = 33;
    public static final int AddScheduleActivity = 34;
    public static final int ScheduleSuggestions = 35;
    public static final int ActivityFragment = 36;
    public static final int CreateAppointment = 37;
    public static final int ViewSchedule = 38;
    public static final int AppointmentList = 39;
    public static final int AppointmentCalendar = 41;

    public static final int Profile = 40;

    public static final int Lines = 50;

    public static final int AddClient = 60;
    public static final int ViewClient = 61;

//    Profile Activity fragments indexes
    public static final int EditProfile = 101;
    public static final int ProfileAddress = 102;
    public static final int Notification = 103;



    public static final String RECEIVER = "geo_address_receiver";
    public static final String FETCH_TYPE_EXTRA = "geo_address_fetch_extra";
    public static final int USE_ADDRESS_NAME = 1;
    public static final String LOCATION_NAME_DATA_EXTRA = "location_name_data_extra";
    public static final int FAILURE_RESULT = -1;
    public static final int SUCCESS_RESULT = 0;
    public static final String RESULT_ADDRESS = "address";
    public static final String RESULT_DATA_KEY = "data_key";

    public static final String RAIN = "rain";
    public static final String CLEAR_DAY = "clear-day";
    public static final String CLEAR_NIGHT = "clear-night";
    public static final String SNOW = "sn`ow";
    public static final String SLEET = "sleet";
    public static final String FOG = "fog";
    public static final String WIND = "wind";
    public static final String PARTLY_CLOUDY_DAY = "partly-cloudy-day";
    public static final String PARTLY_CLOUDY_NIGHT = "partly-cloudy-night";

    public static final String SCHEDULE_CREATED = "a_schedule_exist";
    public static final String CLIENT_CREATED = "a_client_exist";
    public static final String NO_CLIENT_FOUND = "no_client_found";
    public static final String NO_SCHEDULE_FOUND = "no_schedule_found";
    public static final String LINE_CREATED = "a_line_exist";
    public static final String GOOGLE_REVERSE_GEOCODING_URL="https://maps.googleapis.com/maps/api/geocode/json?sensor=true&key=AIzaSyDdWKslIGOEWJMa5RJFwH2dowBc7rEQ14g&latlng=";
    public static PersistentCookieStore MY_COOKIE_STORE=null;
    public static final String USER_LINES = API_PATH + "users/me/lines";
    public static final String USER_LINE_REVENUES = API_PATH + "users/me/lines/{line_id}/revenues/{page_number}";
    public static final String GET_LINE_WITH_ID =  API_PATH + "lines/";
    public static final String USER_LINE_COUNT=  API_PATH + "lines/count";
    public static final String USER_GE0_LINES =  API_PATH + "lines/geo";
    public static File DIR_ROOT = new File("StreamLive"),
            DIR_CACHE = new File("cache"), DIR_HTML_CACHE = new File("html"), DIR_DOWNLOADS = new File("downloads"), DIR_MEDIA = new File("media");

}
