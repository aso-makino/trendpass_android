package com.example.trendpass.async;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class AsyncSetPositionActivity extends AsyncBaseActivity{

    public AsyncSetPositionActivity(Activity activity) {
        super(activity);
    }

    @Override
    protected JSONObject doInBackground(URL... urls) {
        JSONObject resJson = super.doInBackground(urls);
        return resJson;
    }

    protected void onPostExecute(JSONObject resJson) {
        String result = "";
        try {
            result = resJson.getString("greet");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(result);


    }
}

