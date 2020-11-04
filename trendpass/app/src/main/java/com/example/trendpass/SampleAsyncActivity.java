package com.example.trendpass;

import android.app.Activity;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SampleAsyncActivity extends AsyncBaseActivity{

    public SampleAsyncActivity(Activity activity) {
        super(activity);
    }

    @Override
    protected JSONObject doInBackground(URL... urls) {
        JSONObject resJson = super.doInBackground(urls);
        System.out.println(resJson);
        return resJson;
    }

    protected void onPostExecute(JSONObject resJson) {
        String result = "";
        try {
            System.out.println(resJson.getString("str"));
            result = resJson.getString("str");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView tv = activity.findViewById(R.id.messageTextView);
        System.out.println(result);
        tv.setText(result);
    }
}
