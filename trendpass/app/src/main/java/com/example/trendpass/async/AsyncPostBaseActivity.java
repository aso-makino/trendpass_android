package com.example.trendpass.async;

import android.app.Activity;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncPostBaseActivity extends AsyncTask<String, Void, JSONObject> {
    protected Activity activity;

    public AsyncPostBaseActivity(Activity activity) {
        this.activity = activity;
    }

        @Override
        protected JSONObject doInBackground(String... param) {

            String urlst = param[0];
            HttpURLConnection connection = null;
            StringBuilder sb = new StringBuilder();
            try {
                URL url = new URL(urlst);

                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(3000);//接続タイムアウトを設定する。
                connection.setReadTimeout(3000);//レスポンスデータ読み取りタイムアウトを設定する。
                connection.setRequestMethod("POST");//HTTPのメソッドをPOSTに設定する。
                //ヘッダーを設定する
                connection.setRequestProperty("User-Agent", "Android");
                connection.setRequestProperty("Content-Type", "application/octet-stream");
                connection.setDoInput(true);//リクエストのボディ送信を許可する
                connection.setDoOutput(true);//レスポンスのボディ受信を許可する
                connection.setUseCaches(false);//キャッシュを使用しない
                connection.connect();

                // データを投げる
                OutputStream os = connection.getOutputStream();
                final boolean autoFlash = true;
                PrintStream ps = new PrintStream(os, autoFlash, "UTF-8");
                ps.print(param[1]);
                ps.close();

                final int statusCode = connection.getResponseCode();
                if (statusCode != HttpURLConnection.HTTP_OK) {
                    System.err.println("正常に接続できていません。statusCode:" + statusCode);
                    return null;
                }

                final InputStream in = connection.getInputStream();
                String encoding = connection.getContentEncoding();
                if(null == encoding){
                    encoding = "UTF-8";
                }

                // データを受け取る
                final InputStreamReader inReader = new InputStreamReader(in, encoding);
                final BufferedReader bufReader = new BufferedReader(inReader);
                StringBuilder response = new StringBuilder();
                String line = null;

                while((line = bufReader.readLine()) != null) {
                    response.append(line);
                }
                bufReader.close();
                inReader.close();
                in.close();

                JSONObject jsonObject = new JSONObject(response.toString());
                return jsonObject;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
}
