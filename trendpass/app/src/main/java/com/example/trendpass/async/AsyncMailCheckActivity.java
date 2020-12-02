package com.example.trendpass.async;

import android.app.Activity;
import android.widget.EditText;

import com.example.trendpass.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class AsyncMailCheckActivity extends AsyncPostBaseActivity {

    private String url = null;

    public AsyncMailCheckActivity(Activity activity) {
        super(activity);
    }

    @Override
    protected JSONObject doInBackground(String... param) {
        JSONObject resJson = super.doInBackground(param);
        return resJson;
    }

    protected void onPostExecute(final JSONObject resJson) {
        try {
            String result = resJson.getString("res");
            if(result.equals("false")){
                final EditText mailEtxt = activity.findViewById(R.id.mailET);
                mailEtxt.setError("このメールアドレスは使われています");


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}