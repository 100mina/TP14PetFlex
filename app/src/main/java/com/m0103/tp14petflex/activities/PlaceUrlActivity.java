package com.m0103.tp14petflex.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

import com.m0103.tp14petflex.R;
import com.m0103.tp14petflex.databinding.ActivityPlaceUrlBinding;

public class PlaceUrlActivity extends AppCompatActivity {
    ActivityPlaceUrlBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPlaceUrlBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        Intent intent=getIntent();

        binding.placeUrlWeb.setWebViewClient(new WebViewClient());
        binding.placeUrlWeb.setWebChromeClient(new WebChromeClient());
        binding.placeUrlWeb.getSettings().setJavaScriptEnabled(true);
        binding.placeUrlWeb.loadUrl(intent.getStringExtra("placeUrl"));
    }

    @Override
    public void onBackPressed() {
        //웹뷰의 뒤로가기 페이지가 있는지
        if(binding.placeUrlWeb.canGoBack()) binding.placeUrlWeb.goBack();
        else super.onBackPressed();
    }

}