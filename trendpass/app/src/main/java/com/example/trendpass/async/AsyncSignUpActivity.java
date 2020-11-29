package com.example.trendpass.async;

import android.app.Activity;
import android.content.Intent;

import com.example.trendpass.SignUpCompleteActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class AsyncSignUpActivity extends AsyncPostBaseActivity {

    public AsyncSignUpActivity(Activity activity) {
        super(activity);
    }

    @Override
    protected JSONObject doInBackground(String... param) {
        JSONObject resJson = super.doInBackground(param);
        return resJson;
    }

    protected void onPostExecute(JSONObject resJson) {
        String result;

        try {
            result = resJson.getString("result");
            if (result.equals("false")) {
                Intent intent = new Intent(activity, SignUpCompleteActivity.class);
                intent.putExtra("msg","登録に失敗しました");
                activity.startActivity(intent);
            }else{
                Intent intent = new Intent(activity, SignUpCompleteActivity.class);
                intent.putExtra("msg","登録に成功しました");
                activity.startActivity(intent);
            }
            } catch(JSONException e){
                e.printStackTrace();
            }
    }
}
