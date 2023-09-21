package com.m0103.tp14petflex.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.m0103.tp14petflex.R;
import com.m0103.tp14petflex.databinding.ActivityEntryBinding;

public class EntryActivity extends AppCompatActivity {
    ActivityEntryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.entryBtnLogin.setOnClickListener(view -> startActivity(new Intent(this, LoginActivity.class)));
        binding.entryBtnJoin.setOnClickListener(view -> startActivity(new Intent(this, JoinActivity.class)));
        binding.entryBtnNoMember.setOnClickListener(view -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

    }
}