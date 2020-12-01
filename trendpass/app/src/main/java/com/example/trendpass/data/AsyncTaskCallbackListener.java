package com.example.trendpass.data;

import com.example.trendpass.data.model.LoggedInUser;

import org.json.JSONObject;

public interface AsyncTaskCallbackListener {
    public void onPostExecute(JSONObject resJson);
}
