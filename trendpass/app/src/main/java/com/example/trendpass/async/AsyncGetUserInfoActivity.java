package com.example.trendpass.async;

import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.trendpass.R;

import org.json.JSONException;

public class GetUserInfoActivity extends AsyncPostBaseActivity {

    @Override
    protected String doInBackground(String... params) {
        return super.doInBackground(params);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        //edittextの取得
        final EditText nameEtxt = findViewById(R.id.userNameET);
        final EditText mailEtxt = findViewById(R.id.mailET);
        final EditText pass1Etxt = findViewById(R.id.password1ET);
        final EditText pass2Etxt = findViewById(R.id.password2ET);
        final EditText birthEtxt = findViewById(R.id.birthET);

        final RadioGroup group = (RadioGroup)findViewById(R.id.radiogroup_sex);

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
