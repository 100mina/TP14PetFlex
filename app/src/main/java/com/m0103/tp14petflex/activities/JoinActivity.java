package com.m0103.tp14petflex.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.m0103.tp14petflex.R;
import com.m0103.tp14petflex.databinding.ActivityJoinBinding;

public class JoinActivity extends AppCompatActivity {
    ActivityJoinBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityJoinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.joinToolbar.setNavigationOnClickListener(view -> finish());

        binding.joinBtnJoin.setOnClickListener(view -> clickBtnJoin());

    }

    void clickBtnJoin(){
        //TODO: 입력정보 중복확인 -> 서버에 전송

        Toast.makeText(this, "회원가입 완료되었습니다.\n입력하신 정보로 로그인해주세요.", Toast.LENGTH_SHORT).show();
        finish();
    }

}