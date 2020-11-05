package com.example.trendpass;

import android.app.Activity;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class AsyncInsertActivity extends AsyncBaseActivity {

    public AsyncInsertActivity(Activity activity) {
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

        TextView tv = activity.findViewById(R.id.InsertResultTxtv);
        if (result == false){
            tv.setText("登録失敗しました。");
        }else{
            tv.setText("登録成功しました。");
        }
    }
}
