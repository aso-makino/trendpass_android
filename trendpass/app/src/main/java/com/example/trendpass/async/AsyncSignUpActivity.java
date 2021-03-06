package com.example.trendpass.async;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AsyncSignUpActivity extends AsyncTask<Object, Void, JSONObject> {

    //①USE WEEK REFERENCE
    private final Activity activity;

    //②コンストラクタで、 呼び出し元Activityを弱参照で変数セット
    public AsyncSignUpActivity(Activity activity) {
        // USE WEEK REFERENCE
        this.activity = activity;
    }

    //③バックグラウンド処理
    @Override
    protected JSONObject doInBackground(Object... data) {

        //Object配列でパラメータを持ってこれたか確認
        String url = (String) data[0];
        String description = (String) data[1];
        String filePath = (String) data[2];
        String filename = (String) data[5];

        //④HTTP処理用オプジェクト作成
        OkHttpClient client = new OkHttpClient();

        //⑤送信用POSTデータを構築（Multipartでお願いします！）
        final String BOUNDARY = String.valueOf(System.currentTimeMillis());
        final MediaType TEXT = MediaType.parse("application/json; charset=utf-8");
        final MediaType IMAGE = MediaType.parse((String) data[4]);
        RequestBody requestBody = new MultipartBody.Builder(BOUNDARY)
                .setType(MultipartBody.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=Json"),
                        RequestBody.create(TEXT, description)
                )
                .addFormDataPart(
                        "userIcon",
                        filename,
                        RequestBody.create(IMAGE, new File(filePath))
                )
                .build();

        //⑥送信用リクエストを作成
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.post(requestBody);
        Request request = requestBuilder.build();

        //⑦受信用オブジェクトを作成
        Call call = client.newCall(request);
        JSONObject result = null;

        //⑧送信と受信
        try {
            Response response = call.execute();
            ResponseBody body = response.body();
            if (body != null) {
                result = new JSONObject(body.toString());
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        //⑨結果を返し、onPostExecute で受け取る
        return result;
    }

    @Override
    protected void onPostExecute(JSONObject res) {
        super.onPostExecute(res);

        String result;

        try {
            result = res.getString("result");
            if (result.equals("false")) {
                Toast toast = Toast.makeText(activity, "更新に失敗しました", Toast.LENGTH_LONG);
                toast.show();
            } else {
                Toast toast = Toast.makeText(activity, "更新に成功しました", Toast.LENGTH_LONG);
                toast.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}