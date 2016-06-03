package com.tied.android.tiedapp.objects;

/**
 * Created by Emmanuel on 5/30/2016.
 */
public class _Meta {
    private int status_code;
    private String developer_message;
    private String user_message;
    private String response_time;

    public _Meta() {
    }

    public _Meta(int status_code, String developer_message, String user_message, String response_time) {
        this.status_code = status_code;
        this.developer_message = developer_message;
        this.user_message = user_message;
        this.response_time = response_time;
    }

    @Override
    public String toString() {
        return "_Meta{" +
                "status_code=" + status_code +
                ", developer_message='" + developer_message + '\'' +
                ", user_message='" + user_message + '\'' +
                ", response_time='" + response_time + '\'' +
                '}';
    }
}
