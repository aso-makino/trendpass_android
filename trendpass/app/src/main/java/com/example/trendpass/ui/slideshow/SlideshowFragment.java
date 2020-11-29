package com.example.trendpass.ui.slideshow;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.trendpass.MyPageActivity;
import com.example.trendpass.R;
import com.example.trendpass.async.AsyncMyPageReviewActivity;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       /* slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);*/
        View root = inflater.inflate(R.layout.activity_my_page, container, false);
        Picasso.with(getContext())
                .load(R.drawable.settings)
                .fit()
                .centerInside()
                .into((ImageView) root.findViewById(R.id.settings_btn));

        SharedPreferences prefData = getActivity().getSharedPreferences("pref_data", Context.MODE_PRIVATE);
        String userId = prefData.getString("userId", "");

        try {
            String ip= getString(R.string.ip);
            new AsyncMyPageReviewActivity(getActivity())
                    .execute(new URL("http://"+ip+":8080/trendpass/MyPageServlet?userId=" + userId));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        /*final TextView textView = root.findViewById(R.id.text_slideshow);
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }
}
