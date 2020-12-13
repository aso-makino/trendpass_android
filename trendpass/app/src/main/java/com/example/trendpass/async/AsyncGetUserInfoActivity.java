package com.example.trendpass.async;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.example.trendpass.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class AsyncGetUserInfoActivity extends AsyncPostBaseActivity {

    public AsyncGetUserInfoActivity(Activity activity) {
        super(activity);
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject resJson = super.doInBackground(params);
        return resJson;
    }

    protected void onPostExecute(JSONObject res) {

        //edittextの取得
        final EditText nameEtxt = activity.findViewById(R.id.userNameET);
        final EditText mailEtxt = activity.findViewById(R.id.mailET);
        final EditText birthEtxt = activity.findViewById(R.id.birthET);

        final RadioGroup group = (RadioGroup)activity.findViewById(R.id.radiogroup_sex);

        try {
            String name = res.getJSONObject("user").getString("userName");
            String userIcon = res.getJSONObject("user").getString("userIcon");
            String mail = res.getJSONObject("user").getString("mail");
            String sex = res.getJSONObject("user").getString("sex");
            String birth = res.getJSONObject("user").getString("birth");

            //acyncからの戻り値セット
            nameEtxt.setText(name);
            mailEtxt.setText(mail);
            birthEtxt.setText(birth);

            String ip = "ec2-3-112-229-228.ap-northeast-1.compute.amazonaws.com";
            Picasso.with(activity.getApplicationContext())
                    .load("http://" + ip + ":8080/trendpass/DisplayImage?name="+userIcon)
                    .resize(500, 500)
                    .placeholder(R.drawable.noimage)
                    .centerInside()
                    .into((ImageView) activity.findViewById(R.id.userIcon));


            switch (sex){
                case "1":
                    group.check(R.id.radiobutton_male);
                    break;
                case "2":
                    group.check(R.id.radiobutton_female);
                    break;
                case "3":
                    group.check(R.id.radiobutton_unselected);
                    break;
            }
        } catch(JSONException e){
            e.printStackTrace();
        }
    }
}
