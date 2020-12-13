package com.example.trendpass;

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
import java.util.Map;

public class NearBySpotsListActivity<i> extends AppCompatActivity
        implements AdapterView.OnItemClickListener{

    private String[] scenes;
    private String[] datetexts;
    private String[] memos;
    private String[] latitude;
    private String[] longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationinfo_history);

        //インスタンス生成
        SharedPreferences userid_prefs = getSharedPreferences("DATA", Context.MODE_PRIVATE);
        //変数editorは適宜変更
        SharedPreferences.Editor editor = userid_prefs.edit();
        editor.putInt("int", 1);//←書き込み
        editor.apply();
        //UserIdがない場合、初期値0を取得する
        //int userId =　userid_prefs.getInt("int",0);←読み込み
        int userId = 1;//テスト用

        //保存済み位置情報の名前をデバイス内から参照
        //com.example.trendpass.shared_prefs." ここはxmlの名前".xml
        //テスト用
        SharedPreferences restore_info_prefs = getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        SharedPreferences.Editor restore_info = restore_info_prefs.edit();
        //まずはxmlの内部データ数を取得

        //int count = restore_info_prefs.getInt("count",0);
        /*
        // テスト用にデータを挿入
        restore_info.putString("address","博多駅");
        restore_info.putString("date","2020/12/10");
        restore_info.putString("memo","てすと");
        restore_info.putString("latitude","133.133");
        restore_info.putString("longitude","33.33");
        restore_info.putInt("count",count++);
        restore_info.apply();
        restore_info.commit();
        */


        //内部データから情報を全て取得
        restore_info_prefs = getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        Map<String,?> map_name = restore_info_prefs.getAll();
        String key_name;
        Object value_name;
        int count = restore_info_prefs.getInt("count",0);
        ArrayList<String> geoLocationList = new ArrayList<>();
        ArrayList<String> dataList = new ArrayList<>();
        ArrayList<String> memoList = new ArrayList<>();
        ArrayList<String> longitudeList = new ArrayList<>();
        ArrayList<String> latitudeList = new ArrayList<>();

        for(int i = 1; i <= count; i++ ) {
            geoLocationList.add(restore_info_prefs.getString("address"+i, "失敗"));
            scenes = geoLocationList.toArray(new String[i]);
            dataList.add(restore_info_prefs.getString("date"+i, "1970/1/1"));
            datetexts = dataList.toArray(new String[i]);
            memoList.add(restore_info_prefs.getString("memo"+i, "失敗"));
            memos = memoList.toArray(new String[i]);
            longitudeList.add(restore_info_prefs.getString("longitude"+i, "0.0"));
            longitude = longitudeList.toArray(new String[i]);
            latitudeList.add(restore_info_prefs.getString("latitude"+count, "0.0"));
            latitude = latitudeList.toArray(new String[i]);
        }
/*
        for( Map.Entry<String, ?> entry : map_name.entrySet() ) {
            key_name = entry.getKey();
            value_name = entry.getValue();
            for(){
            geoLocationList.add((String) value_name);
        }
        }
/*
        //位置情報の名前をリストから配列に変換
        int element_count = geoLocationList.size();
        System.out.println(element_count);
        scenes = geoLocationList.toArray(new String[0]);

        //文字列変換
        //DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        ////CharSequence dateText  = android.text.format.DateFormat.format("yyyy/MM/dd", Calendar.getInstance());
        //テスト用
        SharedPreferences restore_data_prefs = getSharedPreferences("DATA", Context.MODE_PRIVATE);
        SharedPreferences.Editor restore_data = restore_data_prefs.edit();
        restore_data.putString("string","2020/12/10 0:00");
        restore_data.apply();
        //日付をリストから配列に変換
        //sharedprefはdata型に対応していないので日付をtoStringで文字列として保存したデータを取得
        SharedPreferences  restore_data_pref = PreferenceManager.getDefaultSharedPreferences( this );
        Map<String,?> map_data = restore_data_pref.getAll();
        String key_data;
        Object value_data;
        ArrayList<String> dataList = new ArrayList<>();

        for( Map.Entry<String, ?> entry : map_data.entrySet() )
        {
         key_data = entry.getKey();
         value_data = entry.getValue();
         dataList.add((String) value_data);
        }

        int element_count2 = dataList.size();
        datetexts = dataList.toArray(new String[0]);

        //ユーザーのメモ
            //テスト用
            SharedPreferences restore_memo_prefs = getSharedPreferences("DataStore3", Context.MODE_PRIVATE);
            SharedPreferences.Editor restore_memo = restore_memo_prefs.edit();
            restore_memo.putString("string","テスト用です");
            restore_memo.apply();
        //保存済み位置情報メモをデバイス内から参照
        SharedPreferences  restore_memo_pref = PreferenceManager.getDefaultSharedPreferences( this );
        Map<String,?> map_memo = restore_memo_pref.getAll();
        String key_memo;
        Object value_memo;
        ArrayList<String> memoList = new ArrayList<>();
        for( Map.Entry<String, ?> entry : map_memo.entrySet() )
        {
         key_memo = entry.getKey();
         value_memo = entry.getValue();
         memoList.add((String) value_memo);
        }
        int element_count3 = memoList.size();
        memos = dataList.toArray(new String[0]);



        //位置情報　緯度
        //ユーザーのメモ
        //テスト用
        SharedPreferences restore_latitude_prefs = getSharedPreferences("DataStore4", Context.MODE_PRIVATE);
        SharedPreferences.Editor restore_latitude = restore_latitude_prefs.edit();
        restore_latitude.putString("string","33.55");
        restore_latitude.apply();
        //保存済み位置情報　緯度　をデバイス内から参照

        SharedPreferences  restore_latitude_pref = PreferenceManager.getDefaultSharedPreferences( this );
        Map<String,?> map_latitude = restore_latitude_pref.getAll();
        String key_latitude;
        Object value_latitude;
        ArrayList<String> latitudeList = new ArrayList<>();
        for( Map.Entry<String, ?> entry : map_latitude.entrySet() )
        {
         key_latitude = entry.getKey();
         value_latitude = entry.getValue();
         memoList.add((String) value_latitude);
        }
        int element_count4 = latitudeList.size();
        latitude = dataList.toArray(new String[0]);

        //位置情報　軽度
        //ユーザーのメモ
        //テスト用
        SharedPreferences restore_longitude_prefs = getSharedPreferences("DataStore5", Context.MODE_PRIVATE);
        SharedPreferences.Editor restore_longitude = restore_longitude_prefs.edit();
        restore_longitude.putString("string","133.133");
        restore_longitude.apply();
        //保存済み位置情報　経度　をデバイス内から参照
        SharedPreferences  restore_longitude_pref = PreferenceManager.getDefaultSharedPreferences( this );
        Map<String,?> map_longitude = restore_longitude_pref.getAll();
        String key_longitude;
        Object value_longitude;
        ArrayList<String> longitudeList = new ArrayList<>();
        for( Map.Entry<String, ?> entry : map_longitude.entrySet() )
        {
         key_longitude = entry.getKey();
         value_longitude = entry.getValue();
         memoList.add((String) value_longitude);
        }
        int element_count5 = longitudeList.size();
        longitude = dataList.toArray(new String[0]);
*/
        // ListViewのインスタンスを生成
        ListView listView = findViewById(R.id.list_view);

        // BaseAdapter を継承したadapterのインスタンスを生成
        // レイアウトファイル list.xml を activity_main.xml に
        // inflate するためにadapterに引数として渡す
        BaseAdapter adapter = new ListViewAdapter(this.getApplicationContext(),
        //        R.layout.activity_list_view_adapter, scenes);
        R.layout.activity_list_view_adapter, scenes, datetexts, memos);

        // ListViewにadapterをセット
        listView.setAdapter(adapter);

        // クリックリスナーをセット
        listView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v,
                            int position, long id) {

        Intent intent = new Intent(
                this.getApplicationContext(), NearSpotListActivity.class);

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
        System.out.println(selectedSpot);
        intent.putExtra("SpotLatitude", selectedLatitude);
        System.out.println(selectedLatitude);
        intent.putExtra("SpotLongitude", selectedLongitude);
        System.out.println(selectedLongitude);


        //NearSpotListActivityへ遷移
        startActivity(intent);
    }
}