package com.m0103.tp14petflex.fragments;

import static android.app.Activity.RESULT_CANCELED;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import com.m0103.tp14petflex.G;
import com.m0103.tp14petflex.R;
import com.m0103.tp14petflex.activities.MainActivity;
import com.m0103.tp14petflex.databinding.FragmentUploadBinding;
import com.m0103.tp14petflex.network.RetrofitHelper;
import com.m0103.tp14petflex.network.RetrofitService;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UploadFragment extends Fragment {
    FragmentUploadBinding binding;
    String imgPath;
    MainActivity ma;
    int imgVisible;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentUploadBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ma=(MainActivity) getActivity();
        imgVisible=0;

        binding.uploadImgCheck.setVisibility(View.INVISIBLE);

        binding.uploadBtnImage.setOnClickListener(view1 -> { //이미지 선택 버튼
            Intent intent=new Intent(MediaStore.ACTION_PICK_IMAGES);

            resultLauncher.launch(intent);
        });

        binding.uploadBtnUpload.setOnClickListener(view1 -> clickUpload());

    }//onViewCreated

    void clickUpload(){
        //서버로 전송할 데이터 ( G.nickname 포함 )
        String petName=binding.uploadInputLayoutName.getEditText().getText().toString();
        String petAge=binding.uploadInputLayoutAge.getEditText().getText().toString();
        String petBreed=binding.uploadInputLayoutBreed.getEditText().getText().toString();

        //필수입력칸 확인
        if(petName.equals("")||imgVisible==0) {
            Toast.makeText(getActivity(), "필수입력 정보를 업로드해주세요!", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> data= new HashMap<>();
        data.put("nickname", G.nickname);
        data.put("pet_name", petName);
        data.put("pet_age", petAge);
        data.put("pet_breed", petBreed);

        //전송할 이미지 파일
        File file=new File(imgPath);
        RequestBody requestBody= RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part part=MultipartBody.Part.createFormData("img",file.getName(),requestBody);

        //레트로핏
        Retrofit retrofit= RetrofitHelper.getRetrofitInstance("http://petflex.dothome.co.kr/");
        RetrofitService retrofitService=retrofit.create(RetrofitService.class);
        Call<String> call=retrofitService.upload(data,part);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String s=response.body();
                if(s.equals("false")) {
                    Toast.makeText(getActivity(), "서버 업로드 중 문제가 발생했습니다\n다시 시도해주세요", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(getActivity(), "자랑하기 완료!", Toast.LENGTH_SHORT).show();
                    ma.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, new BoardFragment()).commit();
                    ma.binding.mainToolbar.setTitle("귀염둥이들 구경하기");
                    ma.binding.mainBnv.setSelectedItemId(R.id.bnv_board);

                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    //이미지 선택 결과
    ActivityResultLauncher<Intent> resultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {
        if(result.getResultCode()==RESULT_CANCELED) return;

        Intent intent=result.getData();
        Uri uri=intent.getData();
        imgPath=getRealPathFromUri(uri);

        binding.uploadImgCheck.setVisibility(View.VISIBLE);
        imgVisible=1;
    });

    //Uri -- > 절대경로로 바꿔서 리턴시켜주는 메소드
    String getRealPathFromUri(Uri uri){
        String[] proj= {MediaStore.Images.Media.DATA};
        CursorLoader loader= new CursorLoader(getActivity(), uri, proj, null, null, null);
        Cursor cursor= loader.loadInBackground();
        int column_index= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result= cursor.getString(column_index);
        cursor.close();
        return  result;
    }

}
