package com.m0103.tp14petflex;

import android.app.Activity;
import android.widget.Toast;

public class BackKeyHandler {
    Activity activity;
    long backKeyPressedTime = 0;
    Toast toast;

    public BackKeyHandler(Activity activity) {
        this.activity = activity;
    }

    void showGuide(){
        Toast.makeText(activity, "\'뒤로\'버튼을 한번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show();
    }

    public void onBackPressed(){
        if(System.currentTimeMillis()> backKeyPressedTime+2000){
            backKeyPressedTime = System.currentTimeMillis();
            toast=Toast.makeText(activity, "\'뒤로\'버튼을 한번 더 누르면 종료됩니다", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if(System.currentTimeMillis()<= backKeyPressedTime+2000){
            G.login=0;
            G.nickname=null;
            activity.finish();
            toast.cancel();
        }
    }

}
