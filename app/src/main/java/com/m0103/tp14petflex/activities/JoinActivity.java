package com.m0103.tp14petflex.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.m0103.tp14petflex.R;
import com.m0103.tp14petflex.databinding.ActivityJoinBinding;
import com.m0103.tp14petflex.network.RetrofitHelper;
import com.m0103.tp14petflex.network.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class JoinActivity extends AppCompatActivity {
    ActivityJoinBinding binding;
    int idCheck=0;
    int nicknameCheck=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityJoinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        binding.joinToolbar.setNavigationOnClickListener(view -> finish());
        binding.joinBtnJoin.setOnClickListener(view -> clickBtnJoin());
        binding.joinBtnIdCheck.setOnClickListener(view -> clickBtnIdCheck());
        binding.joinBtnNicknameCheck.setOnClickListener(view -> clickBtnNicknameCheck());

    }

    void clickBtnJoin(){
        //입력 정보 데이터들 변수에 저장해놓기
        String nickname=binding.joinInputLayoutNickname.getEditText().getText().toString();
        String id=binding.joinInputLayoutId.getEditText().getText().toString();
        String pw=binding.joinInputLayoutPw.getEditText().getText().toString();
        String pwCheck=binding.joinInputLayoutPwCheck.getEditText().getText().toString();

        //회원가입 정보 입력 확인
        if (pw.equals("")) {
            Toast.makeText(this, "가입 정보를 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        } else if (pwCheck.equals("")) {
            Toast.makeText(this, "가입 정보를 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        //중복확인 여부
        if(idCheck==0 || nicknameCheck==0){
            Toast.makeText(this, "중복확인버튼을 클릭해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        //비밀번호 확인 여부
        if(!pw.equals(pwCheck)){
            Toast.makeText(this, "비밀번호가 다릅니다\n다시 입력해주세요", Toast.LENGTH_SHORT).show();
            binding.joinInputLayoutPwCheck.getEditText().setText(null);
            return;
        }

        //가입정보 서버로 보내기
        Retrofit retrofit=RetrofitHelper.getRetrofitInstance("http://petflex.dothome.co.kr/");
        RetrofitService retrofitService=retrofit.create(RetrofitService.class);
        Call<String> call=retrofitService.join(nickname, id, pw);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String s=response.body();

                if(s.equals("false")){
                    Toast.makeText(JoinActivity.this, "회원가입 중 오류가 발생했습니다\n다시 시도해주세요", Toast.LENGTH_SHORT).show();

                }else Toast.makeText(JoinActivity.this, "회원가입을 환영합니다!\n가입정보로 로그인해주세요", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });

        finish();
    }


    void clickBtnIdCheck(){
        String id=binding.joinInputLayoutId.getEditText().getText().toString();

        if(id.equals("")) {
            Toast.makeText(this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit= RetrofitHelper.getRetrofitInstance("http://petflex.dothome.co.kr/");
        RetrofitService retrofitService=retrofit.create(RetrofitService.class);

        Call<String> call=retrofitService.idCheck(id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String s=response.body();

                if(s.equals("false")) {
                    Toast.makeText(JoinActivity.this, "중복된 아이디 입니다", Toast.LENGTH_SHORT).show();
                    binding.joinInputLayoutId.getEditText().selectAll();
                }else {
                    Toast.makeText(JoinActivity.this, "사용 가능한 아이디 입니다", Toast.LENGTH_SHORT).show();
                    binding.joinInputLayoutId.getEditText().setEnabled(false);
                    idCheck=1;
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }


    void clickBtnNicknameCheck(){
        String nickname=binding.joinInputLayoutNickname.getEditText().getText().toString();

        if(nickname.equals("")) {
            Toast.makeText(this, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit= RetrofitHelper.getRetrofitInstance("http://petflex.dothome.co.kr/");
        RetrofitService retrofitService=retrofit.create(RetrofitService.class);

        Call<String> call=retrofitService.nicknameCheck(nickname);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String s=response.body();

                if(s.equals("false")) {
                    Toast.makeText(JoinActivity.this, "중복된 닉네임 입니다", Toast.LENGTH_SHORT).show();
                    binding.joinInputLayoutNickname.getEditText().selectAll();
                }else {
                    Toast.makeText(JoinActivity.this, "사용 가능한 닉네임 입니다", Toast.LENGTH_SHORT).show();
                    binding.joinInputLayoutNickname.getEditText().setEnabled(false);
                    nicknameCheck=1;
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }



}