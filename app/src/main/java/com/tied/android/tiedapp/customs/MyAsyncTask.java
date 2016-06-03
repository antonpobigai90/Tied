package com.tied.android.tiedapp.customs;

import android.os.AsyncTask;
import android.os.Build;

import java.util.concurrent.*;

/**
 * Created by FEMI on 11/22/2015.
 */
public class MyAsyncTask extends AsyncTask<String, Void, String> {

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }
    public void execute() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            int corePoolSize = 15;
            int maximumPoolSize = 20;
            int keepAliveTime = 40;

            BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
            Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
            executeOnExecutor(threadPoolExecutor);

        } else {
            execute("");
        }
    }
}
