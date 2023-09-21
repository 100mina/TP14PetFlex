package com.m0103.tp14petflex.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;
import com.m0103.tp14petflex.R;
import com.m0103.tp14petflex.databinding.ActivityMainBinding;
import com.m0103.tp14petflex.fragments.BoardFragment;
import com.m0103.tp14petflex.fragments.HomeFragment;
import com.m0103.tp14petflex.fragments.PlaceFragment;
import com.m0103.tp14petflex.fragments.UploadFragment;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //실행 시 Home Fragment 연결
        getSupportFragmentManager().beginTransaction().add(R.id.container_fragment, new HomeFragment()).commit();

        //프레그먼트 <--> 바텀네비게이션뷰 연결
        binding.mainBnv.setOnItemSelectedListener(item -> {
            int id=item.getItemId();

            if(id==R.id.bnv_home){
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, new HomeFragment()).commit();
                binding.mainToolbar.setTitle("이 달의 사랑둥이들");
            } else if (id==R.id.bnv_upload) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, new UploadFragment()).commit();
                binding.mainToolbar.setTitle("내새꾸 자랑하기");
            } else if (id==R.id.bnv_board) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, new BoardFragment()).commit();
                binding.mainToolbar.setTitle("귀염둥이들 구경하기");
            } else if (id==R.id.bnv_place) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, new PlaceFragment()).commit();
                binding.mainToolbar.setTitle("우리 애기들을 위한 곳");
            }
            return true;
        });

    }
}