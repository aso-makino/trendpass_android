package com.example.trendpass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class NearBySpotsListActivity<i> extends AppCompatActivity
        implements AdapterView.OnItemClickListener{

    private String userId;


    //保存済み位置情報の名前をデバイス内から参照
//    SharedPreferences  restore_spot = PreferenceManager.getDefaultSharedPreferences( this );
//    Map<String,?> map = restore_spot.getAll();
//    String key;
//    Object value;
    ArrayList<String> geoLocationList = new ArrayList<>();
//    for( Entry<String, ?> entry : map.entrySet() )
//    {
//     key = entry.getKey();
//     value = entry.getValue();
//     geoLocationList.add(value);
//    }

    //位置情報の名前をリストから配列に変換
    int element_count = geoLocationList.size();
    String[] scenes = geoLocationList.toArray(new String[0]);

    //文字列変換
    //DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
    ////CharSequence dateText  = android.text.format.DateFormat.format("yyyy/MM/dd", Calendar.getInstance());
    //日付をリストから配列に変換
    //sharedprefはdata型に対応していないので日付をtoStringで文字列として保存したデータを取得
    //SharedPreferences  restore_data = PreferenceManager.getDefaultSharedPreferences( this );
    //Map<String,?> map = restore_data.getAll();
    //String key;
    //Object value;
    ArrayList<String> dataList = new ArrayList<>();
    //for( Entry<String, ?> entry : map.entrySet() )
    //{
    // key = entry.getKey();
    // value = entry.getValue();
    // dataList.add(value);
    //}
    int element_count2 = dataList.size();
    String[] datetexts = dataList.toArray(new String[0]);

    //ユーザーのメモ
    //保存済み位置情報メモをデバイス内から参照
    //SharedPreferences  restore_spotmemo = PreferenceManager.getDefaultSharedPreferences( this );
    //Map<String,?> map = restore_spotmemo.getAll();
    //String key;
    //Object value;
    ArrayList<String> memoList = new ArrayList<>();
    //for( Entry<String, ?> entry : map.entrySet() )
    //{
    // key = entry.getKey();
    // value = entry.getValue();
    // memoList.add(value);
    //}
    int element_count3 = memoList.size();
    String[] memos = dataList.toArray(new String[0]);



    //位置情報　緯度
    //保存済み位置情報　緯度　をデバイス内から参照

    //SharedPreferences  restore_latitude = PreferenceManager.getDefaultSharedPreferences( this );
    //Map<String,?> map = restore_latitude.getAll();
    //String key;
    //Object value;
    ArrayList<String> latitudeList = new ArrayList<>();
    //for( Entry<String, ?> entry : map.entrySet() )
    //{
    // key = entry.getKey();
    // value = entry.getValue();
    // memoList.add(value);
    //}
    int element_count4 = latitudeList.size();
    String[] latitude = dataList.toArray(new String[0]);

    //位置情報　軽度
    //保存済み位置情報　経度　をデバイス内から参照
    //SharedPreferences  restore_latitude = PreferenceManager.getDefaultSharedPreferences( this );
    //Map<String,?> map = restore_latitude.getAll();
    //String key;
    //Object value;
    ArrayList<String> longitudeList = new ArrayList<>();
    //for( Entry<String, ?> entry : map.entrySet() )
    //{
    // key = entry.getKey();
    // value = entry.getValue();
    // memoList.add(value);
    //}
    int element_count5 = longitudeList.size();
    String[] longitude = dataList.toArray(new String[0]);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationinfo_history);

        //インスタンス生成
        //　ユーザーIDを取得
        SharedPreferences loginData = getSharedPreferences("login_data", MODE_PRIVATE);
        userId = loginData.getString("userId", "");

        // ListViewのインスタンスを生成
        ListView listView = findViewById(R.id.list_view);

        // BaseAdapter を継承したadapterのインスタンスを生成
        // レイアウトファイル list.xml を activity_main.xml に
        // inflate するためにadapterに引数として渡す
        BaseAdapter adapter = new ListViewAdapter(this.getApplicationContext(),
                R.layout.activity_list_view_adapter, scenes);
        //R.layout.activity_list_view_adapter, scenes, datetexts, memos, latitude, longitude);

        // ListViewにadapterをセット
        listView.setAdapter(adapter);

        // クリックリスナーをセット
        listView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v,
                            int position, long id) {

        Intent intent = new Intent(
                this.getApplicationContext(), InsertSpotActivity.class);

        // clickされたpositionのgeometry型の位置情報のID
        //
        //テスト用
        String selectedSpot = scenes[position];
        String selectedDate = datetexts[position];
        String selectedMemo = memos[position];
        String selectedLatitude = latitude[position];
        String selectedLongitude = longitude[position];

        // インテントにセット
        //intent.putExtra("position", selectedPosition);
        //intent.putExtra("Photo", selectedPhoto);
        //テスト用
        intent.putExtra("SpotText", selectedSpot);
        intent.putExtra("SpotDate", selectedDate);
        intent.putExtra("SpotMemo", selectedMemo);
        intent.putExtra("SpotLatitude", selectedLatitude);
        intent.putExtra("SpotLongitude", selectedLongitude);


        // InsertSpotActivityへ遷移
        startActivity(intent);
    }
}