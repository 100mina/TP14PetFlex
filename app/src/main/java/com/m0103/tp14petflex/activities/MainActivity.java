package com.m0103.tp14petflex.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationBarView;
import com.m0103.tp14petflex.G;
import com.m0103.tp14petflex.R;
import com.m0103.tp14petflex.data.KakaoResponse;
import com.m0103.tp14petflex.databinding.ActivityMainBinding;
import com.m0103.tp14petflex.fragments.BoardFragment;
import com.m0103.tp14petflex.fragments.HomeFragment;
import com.m0103.tp14petflex.fragments.PlaceFragment;
import com.m0103.tp14petflex.fragments.UploadFragment;
import com.m0103.tp14petflex.network.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //실행 시 Home Fragment 연결
        getSupportFragmentManager().beginTransaction().add(R.id.container_fragment, new HomeFragment()).commit();

        //프레그먼트 <--> 바텀네비게이션뷰 연결
        binding.mainBnv.setOnItemSelectedListener(item -> {
            int id=item.getItemId();

            if(id==R.id.bnv_home){
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, new HomeFragment()).commit();
                binding.mainToolbar.setTitle("이 달의 사랑둥이들");
            } else if (id==R.id.bnv_upload) {
                //회원->UploadFragment , 비회원->LoginActivity
                if(G.login==0){
                    Intent intent=new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(this, "로그인이 필요해요", Toast.LENGTH_SHORT).show();
                }else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, new UploadFragment()).commit();
                    binding.mainToolbar.setTitle("내새꾸 자랑하기");
                }

            } else if (id==R.id.bnv_board) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, new BoardFragment()).commit();
                binding.mainToolbar.setTitle("귀염둥이들 구경하기");
            } else if (id==R.id.bnv_place) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, new PlaceFragment()).commit();
                binding.mainToolbar.setTitle("우리 애기들을 위한 곳");
            }
            return true;
        });

        //위치정보 퍼미션
        int permissionState = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionState == PackageManager.PERMISSION_DENIED) { //퍼미션 거부상태 -> 퍼미션 요청
            resultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }

    }//onCreate method

    ActivityResultLauncher<String> resultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if(!result) Toast.makeText(this, "위치를 제공하지 않아 정보 기능을 사용할 수 없습니다", Toast.LENGTH_SHORT).show();
    });



}//MainActivity
