package com.example.trendpass;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trendpass.async.AsyncNearSpotListActivity;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

public class NearSpotListActivity extends AppCompatActivity {
    AsyncNearSpotListActivity asyncNearSpotListActivity;
    private String latitude;
    private String longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_spot_list);

        Intent intent = getIntent();
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");

        /////////////////////////////////////////////////////////
        //テスト用
        latitude = "35.681300";
        longitude= "139.767165";

        try {
            String ip= getString(R.string.ip);
            AsyncNearSpotListActivity asyncNearSpotListActivity = new AsyncNearSpotListActivity(NearSpotListActivity.this);
            asyncNearSpotListActivity.execute(new URL("http://"+ip+":8080/trendpass/NearSpotList?latitude=" + latitude+"&longitude=" + longitude));


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        ListView listView = (ListView)findViewById(R.id.spotList);

        //リスト項目をクリック時に呼び出されるコールバックを登録
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //リスト項目クリック時の処理
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // clickされたpositionのtextとphotoのID
                String spotId = asyncNearSpotListActivity.getSpotId(position);
                double latitude = asyncNearSpotListActivity.getLatitude(position);
                double longitude = asyncNearSpotListActivity.getLongitude(position);


//                Intent intent = new Intent(NearSpotListActivity.this, InsertReviewActivity.class);
//                // インテントにセット
//                intent.putExtra("spotId", spotId);
//                intent.putExtra("latitude", latitude);//緯度
//                intent.putExtra("longitudo", longitude);//経度
//                // Activity をスイッチする
//                startActivity(intent);

                //今回は、トースト表示
                ListView listView =(ListView)parent;
                String item=(String)listView.getItemAtPosition(position);
                Toast.makeText(NearSpotListActivity.this, "Click: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        Button insertSpotBtn = (Button)findViewById(R.id.InsertSpotBtn);
        insertSpotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(NearSpotListActivity.this, InsertSpotActivity.class);
//                // インテントにセット
//                intent.putExtra("latitude", latitude);//緯度
//                intent.putExtra("longitudo", longitude);//経度
//                // Activity をスイッチする
//                startActivity(intent);
            }
        });

        //footerの生成
        ImageButton mapButton = findViewById(R.id.mapbtn);
        ImageButton insertButton = findViewById(R.id.insertbtn);
        ImageButton mapListButton = findViewById(R.id.listbtn);
        ImageButton userButton = findViewById(R.id.userbtn);

        //ユーザーボタンをタッチした時の処理
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //ボタンタッチしてユーザー設定画面へ
            public void onClick(View view) {
                //設定画面へ
                Intent intent = new Intent(NearSpotListActivity.this, MyPageActivity.class);
                startActivity(intent);
            }
        });

        //投稿ボタンをタッチした時の処理
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //ボタンタッチして投稿種類選択ダイアログ表示
            public void onClick(View view) {

                new AlertDialog.Builder(NearSpotListActivity.this)
                        .setPositiveButton("現在地からスポット投稿", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ////////////////////////////
                                //現在地周辺スポット一覧画面へ
//                                Intent intent = new Intent(NearSpotListActivity.this, NearSpotListActivity.class);
//                                intent.putExtra("latitude",latitude);
//                                intent.putExtra("longitude",longitude);
//                                startActivity(intent);
//                                Log.v("Alert", "スポット一覧へ");
                                ///////////////////////////
                            }
                        })



                        .setNeutralButton("メモしたスポットから投稿", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                ////////////////////////////
                                //位置情報履歴画面へ
//                        Intent intent = new Intent(NearSpotListActivity.this,Activity.class);
//                        startActivity(intent);

                            }
                        })
                        // 口コミ投稿ボタンの処理
                        .setNegativeButton("口コミ投稿", new DialogInterface.OnClickListener() {
                            @Override
                            //口コミ投稿ボタンをタッチ
                            public void onClick(DialogInterface dialog, int which) {
                                ////////////////////////////
                                //口コミ投稿画面へ
//                                Intent intent = new Intent(NearSpotListActivity.this, Activity.class);
//                                startActivity(intent);
                            }
                        })
                        .create()
                        .show();
            }
        });


        //スポットリストボタンをタッチした時の処理
        mapListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //ボタンタッチスポット一覧表示画面へ遷移する
            public void onClick(View view) {
                ///////////////////////////
                //現在地周辺スポット一覧画面へ
                Intent intent = new Intent(NearSpotListActivity.this, DispSpotListActivity.class);
                startActivity(intent);
                Log.v("Alert", "スポット一覧へ");
            }
        });

        //マップボタンをタッチした時の処理
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //ボタンタッチしてユーザー設定画面へ
            public void onClick(View view) {
                //設定画面へ
                Intent intent = new Intent(NearSpotListActivity.this, DispMapActivity.class);
                startActivity(intent);
            }
        });

    }
}
