package com.m0103.tp14petflex.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.m0103.tp14petflex.G;
import com.m0103.tp14petflex.adapters.HomeRecyclerAdapter;
import com.m0103.tp14petflex.data.BoardData;
import com.m0103.tp14petflex.databinding.FragmentHomeBinding;
import com.m0103.tp14petflex.network.RetrofitHelper;
import com.m0103.tp14petflex.network.RetrofitService;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    ArrayList<BoardData> boardDataArrayList =new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentHomeBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO: 나도 자랑하러 가기 버튼 -> 자랑하기 탭

        Retrofit retrofit= RetrofitHelper.getRetrofitInstance("http://petflex.dothome.co.kr/");
        RetrofitService retrofitService=retrofit.create(RetrofitService.class);
        Call<ArrayList<BoardData>> call=retrofitService.loadRank(G.start.toString(),G.end.toString());
        call.enqueue(new Callback<ArrayList<BoardData>>() {
            @Override
            public void onResponse(Call<ArrayList<BoardData>> call, Response<ArrayList<BoardData>> response) {
                ArrayList<BoardData> items=response.body();
                for(int i=0; i<items.size(); i++){
                    BoardData item=items.get(i);
                    boardDataArrayList.add(item);
                }

                //RecyclerView <-> Adapter 연결 / GridLayout 속성 추가
                HomeRecyclerAdapter adapter=new HomeRecyclerAdapter(getActivity(), boardDataArrayList);
                GridLayoutManager layoutManager=new GridLayoutManager(getActivity(), 2);
                layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if(position==0) return 2; //return 값 : 아이템이 먹을 칸 수, 부피
                        else return 1;
                    }
                });
                binding.recyclerViewHome.setLayoutManager(layoutManager);
                binding.recyclerViewHome.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<BoardData>> call, Throwable t) {
            }
        });




    }


}
