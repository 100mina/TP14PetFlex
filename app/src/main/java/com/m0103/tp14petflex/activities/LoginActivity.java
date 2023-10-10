package com.m0103.tp14petflex.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.m0103.tp14petflex.G;
import com.m0103.tp14petflex.R;
import com.m0103.tp14petflex.databinding.ActivityLoginBinding;
import com.m0103.tp14petflex.network.RetrofitHelper;
import com.m0103.tp14petflex.network.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        binding.loginToolbar.setNavigationOnClickListener(view -> finish());
        binding.loginBtnLogin.setOnClickListener(view -> clickBtnLogin());

        binding.loginBtnGoJoin.setOnClickListener(view -> startActivity(new Intent(this, JoinActivity.class)));
    }

    void clickBtnLogin(){
        String id=binding.loginInputLayoutId.getEditText().getText().toString();
        String password=binding.loginInputLayoutPw.getEditText().getText().toString();

        Retrofit retrofit= RetrofitHelper.getRetrofitInstance("http://petflex.dothome.co.kr/");
        RetrofitService retrofitService=retrofit.create(RetrofitService.class);
        Call<String> call=retrofitService.login(id,password);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String s=response.body();

                if(s.equals("")){
                    Toast.makeText(LoginActivity.this, "아이디, 비밀번호가 다릅니다\n입력 정보를 확인해주세요", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(LoginActivity.this, s+"님 환영해요!", Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    G.nickname=s;
                    G.login=1;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


}