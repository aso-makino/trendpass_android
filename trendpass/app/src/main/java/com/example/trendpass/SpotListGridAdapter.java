package com.example.trendpass;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.content.Context.WINDOW_SERVICE;

public class SpotListGridAdapter extends BaseAdapter {

    private List<HashMap<String,String>> spotReviewList;
    private LayoutInflater inflater;
    private int layoutId;
    private Context context;
    private List<String> imageList = new ArrayList<>();
    private int ScreenWHalf = 0;

    public SpotListGridAdapter(Context context,
                       int layoutId, String[] image,
                               List<HashMap<String, String>> spotReviewList) {
        super();
        this.inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutId = layoutId;
        this.spotReviewList = spotReviewList;
        this.context = context;
        Collections.addAll(imageList, image);

        // 画面の横幅の半分を計算
        WindowManager wm = (WindowManager)
                context.getSystemService(WINDOW_SERVICE);
        if(wm != null){
            Display disp = wm.getDefaultDisplay();
            Point size = new Point();
            disp.getSize(size);

            int screenWidth = size.x;
            ScreenWHalf = screenWidth/3;
            Log.d("debug","ScreenWidthHalf="+ScreenWHalf);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            view = inflater.inflate(layoutId, parent, false);
        } else {
            view =  convertView;
        }

        ImageView img = view.findViewById(R.id.mypage_image);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);



        System.out.println(1);
        Picasso.with(context)
                .load(addUrl(position))
                .resize(ScreenWHalf, ScreenWHalf)
                .placeholder(R.drawable.insert)
                .error(R.drawable.map)
                .centerCrop()
                .into(img);


        view.findViewById(R.id.mypage_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = imageList.get(position) + "が選択されました。";
                System.out.println(message);
                Intent intent = new Intent(context,DispSpotDetailActivity.class);
                intent.putExtra("spotId", spotReviewList.get(position).get("spotId"));
                intent.putExtra("spotName", spotReviewList.get(position).get("spotName"));
                intent.putExtra("latitude", spotReviewList.get(position).get("latitude"));
                intent.putExtra("longitude", spotReviewList.get(position).get("longitude"));
                intent.putExtra("genreId", spotReviewList.get(position).get("genreId"));
                intent.putExtra("reviewImage", spotReviewList.get(position).get("reviewImage"));
                context.startActivity(intent);
            }
        });

        return view;
    }

    // ネットワークアクセスするURLを設定する
    private String addUrl(int number){

        String ip = "ec2-3-112-229-228.ap-northeast-1.compute.amazonaws.com";
        return String.format(Locale.US,
                "http://" + ip + ":8080/trendpass/DisplayImage?name=%s" ,// 自分のサーバーに上げて見ましょう
                imageList.get(number));
    }


    @Override
    public int getCount() {
        // 全要素数を返す
        return imageList.size();
    }
    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
