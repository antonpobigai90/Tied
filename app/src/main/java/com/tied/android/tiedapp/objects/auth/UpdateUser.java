package com.tied.android.tiedapp.objects.auth;

/**
 * Created by Emmanuel on 5/30/2016.
 */
public class UpdateUser {

    private boolean success;
    private String message;

    public UpdateUser() {
    }

    public UpdateUser(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "UpdateUser{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
