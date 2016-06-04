package com.tied.android.tiedapp.util;

import android.app.Activity;
import android.content.Context;

/**
 * Created by Maulik.Joshi on 29-06-2015.
 */
public class DialogUtils {
	public static AppDialog appDialog;
    public static ProgressIndicator progressIndicator;

    public static void openDialog(Context context, String message,String positiveButton, String negativeButton){
		appDialog = new AppDialog(context, message, positiveButton, negativeButton);
		appDialog.show();
	}
	    
    public static void displayProgress(Context context){
        if(progressIndicator == null){
            progressIndicator = new ProgressIndicator(context);
        }

        if(!((Activity)context).isFinishing() && !progressIndicator.isShowing())
            progressIndicator.show();
    }

    public static void closeProgress(){
        if (progressIndicator != null && progressIndicator.isShowing()) {
            progressIndicator.dismiss();
            progressIndicator = null;
        }
    }
}
