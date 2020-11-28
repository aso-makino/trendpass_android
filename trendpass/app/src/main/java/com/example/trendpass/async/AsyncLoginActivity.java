package com.example.trendpass.async;

import android.app.Activity;

import com.example.trendpass.data.AsyncTaskCallbackListener;

import org.json.JSONObject;

import java.net.URL;

public class AsyncLoginActivity extends AsyncBaseActivity {

    private AsyncTaskCallbackListener listener = null;

    public AsyncLoginActivity(Activity activity, AsyncTaskCallbackListener listener) {
        super(activity);
        this.listener = listener;
    }

    @Override
    protected JSONObject doInBackground(URL... urls) {
        JSONObject resJson = super.doInBackground(urls);
        return resJson;
    }

    protected void onPostExecute(JSONObject resJson) {
        if (listener != null) {
            listener.onPostExecute(resJson);
        }
    }
}

