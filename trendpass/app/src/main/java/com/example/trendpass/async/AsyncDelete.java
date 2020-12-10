package com.example.trendpass.async;

import android.app.Activity;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trendpass.CompleteUnsubscribeActivity;
import com.example.trendpass.ConfirmUnsubscribeActivity;
import com.example.trendpass.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class AsyncDelete extends AsyncBaseActivity {

    public AsyncDelete(Activity activity) {
        super(activity);
    }

    @Override
    protected JSONObject doInBackground(URL... urls) {
        JSONObject resJson = super.doInBackground(urls[0]);
        return resJson;
    }

    protected void onPostExecute(JSONObject resJson) {
        String result;

        try {
            result = resJson.getString("result");
            if (result.equals("false")) {
                Toast toast = Toast.makeText(activity, "退会に失敗しました", Toast.LENGTH_LONG);
                toast.show();
            } else {
                Intent intent = new Intent(activity, CompleteUnsubscribeActivity.class);
                activity.startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
