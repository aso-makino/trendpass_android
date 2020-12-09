package com.example.trendpass.async;

import android.app.Activity;

import org.json.JSONObject;

import java.net.URL;

public class AsyncDispMapActivity extends AsyncBaseActivity {

    private String url = null;

    public AsyncDispMapActivity(Activity activity) {
        super(activity);
    }


    @Override
    protected JSONObject doInBackground(URL... urls) {

        this.url = urls[0].toString();
        JSONObject resJson = super.doInBackground(urls[0]);
        return resJson;
    }


    protected void onPostExecute(JSONObject resJson) {



        }
    }
