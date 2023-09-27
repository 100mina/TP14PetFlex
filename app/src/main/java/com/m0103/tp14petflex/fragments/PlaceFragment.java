package com.m0103.tp14petflex.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.m0103.tp14petflex.R;
import com.m0103.tp14petflex.activities.MainActivity;
import com.m0103.tp14petflex.adapters.PlaceRecyclerAdapter;
import com.m0103.tp14petflex.data.KakaoResponse;
import com.m0103.tp14petflex.databinding.FragmentPlaceBinding;
import com.m0103.tp14petflex.databinding.RecyclerPlaceBinding;
import com.m0103.tp14petflex.network.RetrofitHelper;
import com.m0103.tp14petflex.network.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlaceFragment extends Fragment {
    FragmentPlaceBinding binding;
    MainActivity ma;
    public Location myLocation = null;
    public int pageNumber = 1;
    public KakaoResponse kakaoResponse=null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPlaceBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestLocation();

        binding.placeBtnHospital.setOnClickListener(view1 -> {
            binding.placeBtnHospital.setTextColor(Color.parseColor("#000000"));
            binding.placeBtnShop.setTextColor(Color.parseColor("#A0A0A0"));
            searchPlace("동물병원");
        });
        binding.placeBtnShop.setOnClickListener(view1 -> {
            binding.placeBtnHospital.setTextColor(Color.parseColor("#A0A0A0"));
            binding.placeBtnShop.setTextColor(Color.parseColor("#000000"));
            searchPlace("반려동물용품");
        });


    }//onViewCreated method

    public void requestLocation(){ //현재 위치 얻어오는 메소드
        if (MainActivity.permissionState == PackageManager.PERMISSION_DENIED) return;
        FusedLocationProviderClient providerClient= LocationServices.getFusedLocationProviderClient(getActivity());

        LocationRequest request = new LocationRequest.Builder(5000).build();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        providerClient.requestLocationUpdates(request, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                myLocation=location;

                //위치를 찾은 후 더이상 업데이트 X
                providerClient.removeLocationUpdates(this); //리스너 객체 본인을 삭제

                searchPlace("동물병원");
            }
        }, Looper.getMainLooper());

    }//requestLocation method

    public void searchPlace(String query) {
        if (MainActivity.permissionState == PackageManager.PERMISSION_DENIED) return;

        Retrofit retrofit = RetrofitHelper.getRetrofitInstance("https://dapi.kakao.com");
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);

        Call<KakaoResponse> call = retrofitService.searchPlaceKakao(query, ""+myLocation.getLongitude(),
                ""+myLocation.getLatitude(), pageNumber);
        call.enqueue(new Callback<KakaoResponse>() {
            @Override
            public void onResponse(Call<KakaoResponse> call, Response<KakaoResponse> response) {
                kakaoResponse = response.body();

                PlaceRecyclerAdapter adapter = new PlaceRecyclerAdapter(getActivity(), kakaoResponse.documents);
                binding.recyclerViewPlace.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<KakaoResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "error...", Toast.LENGTH_SHORT).show();
            }
        });


    }//searchPlace method



}//PlaceFragment.class
