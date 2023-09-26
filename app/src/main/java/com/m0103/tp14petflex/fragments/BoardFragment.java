package com.m0103.tp14petflex.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.m0103.tp14petflex.adapters.BoardRecyclerAdapter;
import com.m0103.tp14petflex.data.BoardData;
import com.m0103.tp14petflex.databinding.FragmentBoardBinding;
import com.m0103.tp14petflex.network.RetrofitHelper;
import com.m0103.tp14petflex.network.RetrofitService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BoardFragment extends Fragment {
    FragmentBoardBinding binding;
    ArrayList<BoardData> boardDataArrayList= new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentBoardBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadData();

    }

    public void loadData(){ //서버에서 데이터 불러오기
        Retrofit retrofit= RetrofitHelper.getRetrofitInstance("http://petflex.dothome.co.kr/");
        RetrofitService retrofitService=retrofit.create(RetrofitService.class);
        Call<ArrayList<BoardData>> call=retrofitService.load();
        call.enqueue(new Callback<ArrayList<BoardData>>() {
            @Override
            public void onResponse(Call<ArrayList<BoardData>> call, Response<ArrayList<BoardData>> response) {

                ArrayList<BoardData> items=response.body();
                for(int i=0; i<items.size(); i++){
                    BoardData item=items.get(i);
                    boardDataArrayList.add(item);
                }

                BoardRecyclerAdapter adapter= new BoardRecyclerAdapter(getActivity(), boardDataArrayList);
                binding.recyclerViewBoard.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<ArrayList<BoardData>> call, Throwable t) {

            }
        });
    }//loadData


}//boardFragment
