package com.example.trendpass;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

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

        //ロゴ
        Picasso.with(this.getApplicationContext())
                .load(R.drawable.rogo)
                .resize(500,500)
                .placeholder(R.drawable.noimage)
                .centerInside()
                .into((ImageView) this.findViewById(R.id.rogoImg));

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

        findViewById(R.id.mapbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("mapが押されました");
            }
        });

        findViewById(R.id.insertbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("insertが押されました");
            }
        });

        findViewById(R.id.listbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("listが押されました");
            }
        });

        findViewById(R.id.userbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("userが押されました");
            }
        });

    }
}
