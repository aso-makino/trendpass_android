package com.example.trendpass.async;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.trendpass.CompleteUnsubscribeActivity;
import com.example.trendpass.DispMapActivity;
import com.example.trendpass.DispSpotListActivity;
import com.example.trendpass.MyPageActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AsyncInsert extends AsyncTask<String, Void, JSONObject> {

    //①USE WEEK REFERENCE
    private final Activity activity;

    public AsyncInsert(Activity activity) {
        this.activity = activity;
    }

    //③バックグラウンド処理
    @Override
    protected JSONObject doInBackground(String... data) {

        //Object配列でパラメータを持ってこれたか確認
        String url = (String) data[0];
        String description = (String) data[1];

        RequestBody requestBody = null;
        //④HTTP処理用オプジェクト作成
        OkHttpClient client = new OkHttpClient();
        //⑤送信用POSTデータを構築（Multipartでお願いします！）
        final String BOUNDARY = String.valueOf(System.currentTimeMillis());
        final MediaType TEXT = MediaType.parse("application/json; charset=utf-8");

        if(data[2] != null) {
            String pathName = data[2];
            int endIndex = pathName.lastIndexOf('/'); // 最後の'/'のインデックスを検索
            String fileName = pathName.substring(endIndex + 1); //String pathName = picturePath;

            final MediaType IMAGE = MediaType.parse("image/jpeg");

            requestBody = new MultipartBody.Builder(BOUNDARY)
                    .setType(MultipartBody.FORM)
                    .addPart(
                            Headers.of("Content-Disposition", "form-data; name=\"description\""),
                            RequestBody.create(TEXT, description)
                    )
                    .addFormDataPart(
                            "reviewImage",
                            fileName,
                            RequestBody.create(IMAGE, new File(data[2]))
                    )
                    .build();
        }else{
            requestBody = new MultipartBody.Builder(BOUNDARY)
                    .setType(MultipartBody.FORM)
                    .addPart(
                            Headers.of("Content-Disposition", "form-data; name=\"description\""),
                            RequestBody.create(TEXT, description)
                    )
                    .build();
        }



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
                result = new JSONObject(body.string());
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        //⑨結果を返し、onPostExecute で受け取る
        return result;
    }

    protected void onPostExecute(JSONObject resJson) {
        String result;
        System.out.println(resJson);

        try {
            result = resJson.getString("result");
            if (result.equals("false")) {
                Toast toast = Toast.makeText(activity, "投稿に失敗しました", Toast.LENGTH_LONG);
                toast.show();
            } else {
                Toast toast = Toast.makeText(activity, "投稿完了しました", Toast.LENGTH_LONG);
                toast.show();
                Intent intent = new Intent(activity, MyPageActivity.class);
                activity.startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

