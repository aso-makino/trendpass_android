package com.example.trendpass.async;

import android.app.Activity;
import android.widget.TextView;

import com.example.trendpass.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class AsyncLoginActivity extends AsyncBaseActivity {

    public AsyncLoginActivity(Activity activity) {
        super(activity);
    }

    @Override
    protected JSONObject doInBackground(URL... urls) {
        JSONObject resJson = super.doInBackground(urls);
        return resJson;
    }

    protected void onPostExecute(JSONObject resJson) {
        boolean result = false;
        try {
            result = resJson.getBoolean("result");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (result == false){
            TextView tv = activity.findViewById(R.id.LoginResultTxtv);
            tv.setText("メールアドレス又は、パスワードが間違っています。");
        }
    }
}


