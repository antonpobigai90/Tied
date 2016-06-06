package com.tied.android.tiedapp.objects.auth;

/**
 * Created by Emmanuel on 5/30/2016.
 */
public class ServerInfo {

    private boolean success;
    private String message;
    private int code;

    public ServerInfo() {
    }

    public ServerInfo(boolean success, String message, int code) {
        this.success = success;
        this.message = message;
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ServerInfo{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
