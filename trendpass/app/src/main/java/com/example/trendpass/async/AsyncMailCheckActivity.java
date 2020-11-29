package com.example.trendpass.async;

import android.app.Activity;
import android.widget.EditText;

import com.example.trendpass.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class AsyncMailCheckActivity extends AsyncBaseActivity {

    private String url = null;

    public AsyncMailCheckActivity(Activity activity) {
        super(activity);
    }

    @Override
    protected JSONObject doInBackground(URL... urls) {
        this.url = urls[0].toString();
        JSONObject resJson = super.doInBackground(urls);
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