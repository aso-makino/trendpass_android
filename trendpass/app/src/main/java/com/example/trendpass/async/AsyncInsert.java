package com.example.trendpass.async;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class AsyncInsert extends AsyncBaseActivity {

    public AsyncInsert(Activity activity) {
        super(activity);
    }

    @Override
    protected JSONObject doInBackground(URL... urls) {
        JSONObject resJson = super.doInBackground(urls[0]);
        return resJson;
    }

    protected void onPostExecute(JSONObject resJson) {
        boolean result;
        String result_string = "成功";
        try {
            result = resJson.getBoolean("isSelect");

        } catch (JSONException e) {
            e.printStackTrace();
            result_string = "失敗";
        }
        /*
        TextView tv = activity.findViewById(R.id.messageTextView);
        tv.setText(result_string);
         */
    }
}
