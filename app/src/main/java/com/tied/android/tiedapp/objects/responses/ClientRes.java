package com.tied.android.tiedapp.objects.responses;

import com.tied.android.tiedapp.objects.Client;
import com.tied.android.tiedapp.objects._Meta;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Emmanuel on 6/28/2016.
 */
public class ClientRes implements Serializable {
    private boolean success;
    private String message;
    private boolean authFailed;
    private _Meta _meta;
    private ArrayList<Client> clients;

    public ClientRes(boolean success, String message, boolean authFailed, _Meta _meta, ArrayList<Client> clients) {
        this.success = success;
        this.message = message;
        this.authFailed = authFailed;
        this._meta = _meta;
        this.clients = clients;
    }



    public _Meta get_meta() {
        return _meta;
    }

    public void set_meta(_Meta _meta) {
        this._meta = _meta;
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public void setClients(ArrayList<Client> clients) {
        this.clients = clients;
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

    public boolean isAuthFailed() {
        return authFailed;
    }

    public void setAuthFailed(boolean authFailed) {
        this.authFailed = authFailed;
    }

    @Override
    public String toString() {
        return "ClientRes{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", authFailed=" + authFailed +
                ", _meta=" + _meta +
                ", clients=" + clients +
                '}';
    }
}
