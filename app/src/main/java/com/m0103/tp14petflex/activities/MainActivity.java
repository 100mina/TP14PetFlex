package com.m0103.tp14petflex.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationBarView;
import com.m0103.tp14petflex.BackKeyHandler;
import com.m0103.tp14petflex.G;
import com.m0103.tp14petflex.R;
import com.m0103.tp14petflex.data.KakaoResponse;
import com.m0103.tp14petflex.databinding.ActivityMainBinding;
import com.m0103.tp14petflex.fragments.BoardFragment;
import com.m0103.tp14petflex.fragments.HomeFragment;
import com.m0103.tp14petflex.fragments.PlaceFragment;
import com.m0103.tp14petflex.fragments.UploadFragment;
import com.m0103.tp14petflex.network.RetrofitService;

import java.time.LocalDate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public static ActivityMainBinding binding;
    public static int permissionState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //실행 시 Home Fragment 연결
        getSupportFragmentManager().beginTransaction().add(R.id.container_fragment, new HomeFragment()).commit();
        binding.mainToolbar.setNavigationOnClickListener(view -> createDialog(1));
        binding.mainToolbar.setTitle(G.lastMonth+"월의 사랑둥이들");

        //프레그먼트 <--> 바텀네비게이션뷰 연결
        binding.mainBnv.setOnItemSelectedListener(item -> {
            int id=item.getItemId();

            if(id==R.id.bnv_home){
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, new HomeFragment()).commit();
                binding.mainToolbar.setTitle(G.lastMonth+"월의 사랑둥이들");
                binding.mainToolbar.setNavigationOnClickListener(view -> createDialog(1));
            } else if (id==R.id.bnv_upload) {

                //회원->UploadFragment , 비회원->LoginActivity
                if(G.login==0){
                    Intent intent=new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(this, "로그인이 필요한 기능이에요", Toast.LENGTH_SHORT).show();
                }else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, new UploadFragment()).commit();
                    binding.mainToolbar.setTitle("내새꾸 자랑하기");
                    binding.mainToolbar.setNavigationOnClickListener(view -> createDialog(2));
                }

            } else if (id==R.id.bnv_board) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, new BoardFragment()).commit();
                binding.mainToolbar.setTitle("귀염둥이들 구경하기");
                binding.mainToolbar.setNavigationOnClickListener(view ->createDialog(3));

            } else if (id==R.id.bnv_place) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, new PlaceFragment()).commit();
                binding.mainToolbar.setTitle("우리 애기들을 위한 곳");
                binding.mainToolbar.setNavigationOnClickListener(view -> createDialog(4));
            }
            return true;
        });

        //위치정보 퍼미션
        permissionState = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionState == PackageManager.PERMISSION_DENIED) { //퍼미션 거부상태 -> 퍼미션 요청
            resultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }

    }//onCreate method

    ActivityResultLauncher<String> resultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if(!result) Toast.makeText(this, "위치를 제공하지 않아 내 위치 기반 정보 기능을 사용할 수 없어요", Toast.LENGTH_SHORT).show();
    });

    void createDialog(int i){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(R.layout.dialog_main_help);
        AlertDialog dialog=builder.create();
        dialog.show();

        TextView tv=dialog.findViewById(R.id.tv_help);

        switch (i){
            case 1:
                tv.setText(("이전 한달 간의 좋아요 갯수\nTOP 10 목록이에요\n\n" +
                        "스크롤을 마지막까지 내리면\n총 좋아요 TOP 10이나\n" +
                        "오늘 날짜를 기준으로 일주일 동안의\nTOP 10 순위도 볼 수 있어요!\n\n" +
                        "(원래 목록으로 -> 홈탭 클릭!)\n\n" +
                        "자랑 탭을 클릭해서\n내 반려동물을 자랑할 수 있어요\n\nTOP 10을 노려보세요!"));
                break;
            case 2:
                tv.setText("내 반려동물의 정보를 작성하고\n사진 첨부하기 버튼을 클릭해서\n사진을 설정해주세요\n\n" +
                    "사진이 설정되었다면 버튼 옆에\n체크표시가 나타나요\n\n필수정보(이름, 사진)를 모두 입력 후" +
                    "\n자랑시작 버튼을 클릭하면\n글 작성 완료!\n\n모든 게시물은 구경 탭에서\n확인할 수 있어요");
                break;
            case 3:
                tv.setText("모든 게시물을 최신순/좋아요 순으로 볼 수 있어요\n\n더 자세히 보고싶다면 게시물 클릭!\n\n" +
                        "오른쪽 하단 하트 버튼 클릭으로\n좋아요/좋아요취소 기능을 표시할 수 있어요" +
                        "\n\n사랑스러운 아이가 있다면\n좋아요를 표시해주세요!" +
                        "\n\n(좋아요 기능은 회원만 사용할 수 있어요)" +
                        "\n\n앱 취지에 맞지 않는 게시물이 있다면\n꾹 눌러서 신고 버튼으로 알려주세요" +
                        "\n검토 후 빠르게 조치하겠습니다!\n\n" +
                        "--내 게시물 삭제 방법--\n내 게시물 -> 해당 게시물 꾹 눌러서 \'삭제\' 버튼 클릭");
                break;
            case 4:
                tv.setText("내 위치 기반 동물병원,\n반려동물 용품점 정보를 볼 수 있어요\n\n" +
                        "더 자세히 보고싶은 정보가 있다면\n클릭해서 상세페이지를 볼 수 있어요" +
                        "\n\n만약 위치정보제공을\n허용하지 않았다면\n이 기능은 이용할 수 없어요\n\n--위치 정보 허용 방법--\n" +
                        "휴대전화 설정 -> 애플리케이션 ->\n펫플릭스 -> 위치 권한 허용");
                break;
        }
    }

    BackKeyHandler backKeyHandler=new BackKeyHandler(this);
    @Override
    public void onBackPressed() {
        backKeyHandler.onBackPressed();
    }

}//MainActivity
