package com.example.trendpass.async;

import android.app.Activity;
import android.widget.TextView;

import com.example.trendpass.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class SampleAsyncActivity extends AsyncBaseActivity{

    public SampleAsyncActivity(Activity activity) {
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
            result = resJson.getString("str");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView tv = activity.findViewById(R.id.messageTextView);
        tv.setText(result);
    }
}
