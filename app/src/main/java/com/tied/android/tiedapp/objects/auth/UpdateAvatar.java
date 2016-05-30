package com.tied.android.tiedapp.objects.auth;

/**
 * Created by Emmanuel on 5/30/2016.
 */
public class UpdateAvatar {

    private boolean success;
    private String message;
    private String avatar;

    public UpdateAvatar() {
    }

    public UpdateAvatar(boolean success, String message, String avatar) {
        this.success = success;
        this.message = message;
        this.avatar = avatar;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "UpdateAvatar{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
