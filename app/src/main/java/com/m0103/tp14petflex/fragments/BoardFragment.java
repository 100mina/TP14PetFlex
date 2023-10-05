package com.m0103.tp14petflex.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.m0103.tp14petflex.G;
import com.m0103.tp14petflex.activities.MainActivity;
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
    BoardRecyclerAdapter adapter;

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

        binding.boardBtnLatest.setOnClickListener(view1 -> loadData());
        binding.boardBtnCountFav.setOnClickListener(view1 -> loadTotalFav());
        binding.boardBtnMyPost.setOnClickListener(view1 -> loadMyPost());

    }

    public void loadData(){ //서버에서 데이터 불러오기
        Retrofit retrofit= RetrofitHelper.getRetrofitInstance("http://petflex.dothome.co.kr/");
        RetrofitService retrofitService=retrofit.create(RetrofitService.class);
        Call<ArrayList<BoardData>> call=retrofitService.load();
        call.enqueue(new Callback<ArrayList<BoardData>>() {
            @Override
            public void onResponse(Call<ArrayList<BoardData>> call, Response<ArrayList<BoardData>> response) {
                boardDataArrayList.clear();

                ArrayList<BoardData> items=response.body();
                for(int i=0; i<items.size(); i++){
                    BoardData item=items.get(i);
                    boardDataArrayList.add(0,item);
                }

                adapter= new BoardRecyclerAdapter(getActivity(), boardDataArrayList);
                binding.recyclerViewBoard.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<ArrayList<BoardData>> call, Throwable t) {

            }
        });
    }//loadData

    public void loadTotalFav(){ //좋아요 순으로 데이터 불러오기
        Retrofit retrofit = RetrofitHelper.getRetrofitInstance("http://petflex.dothome.co.kr/");
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<ArrayList<BoardData>> call1 = retrofitService.loadTotalFav();
        call1.enqueue(new Callback<ArrayList<BoardData>>() {
            @Override
            public void onResponse(Call<ArrayList<BoardData>> call, Response<ArrayList<BoardData>> response) {
                boardDataArrayList.clear();

                ArrayList<BoardData> items = response.body();
                for (int i = 0; i < items.size(); i++) {
                    BoardData item = items.get(i);
                    boardDataArrayList.add(item);
                }
                adapter= new BoardRecyclerAdapter(getActivity(), boardDataArrayList);
                binding.recyclerViewBoard.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<ArrayList<BoardData>> call, Throwable t) {
            }
        });
    }//loadTotalFav

    public void loadMyPost(){
        if(G.login==0) {
            Toast.makeText(getActivity(), "작성한 게시물이 없어요", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = RetrofitHelper.getRetrofitInstance("http://petflex.dothome.co.kr/");
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<ArrayList<BoardData>> call1 = retrofitService.loadMyPost(G.nickname);
        call1.enqueue(new Callback<ArrayList<BoardData>>() {
            @Override
            public void onResponse(Call<ArrayList<BoardData>> call, Response<ArrayList<BoardData>> response) {
                boardDataArrayList.clear();

                ArrayList<BoardData> items = response.body();

                if(items.size()==0) {
                    Toast.makeText(getActivity(), "작성한 게시물이 없어요", Toast.LENGTH_SHORT).show();
                }

                for (int i = 0; i < items.size(); i++) {
                    BoardData item = items.get(i);
                    boardDataArrayList.add(0,item);
                }
                adapter= new BoardRecyclerAdapter(getActivity(), boardDataArrayList,"myPost");
                binding.recyclerViewBoard.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<BoardData>> call, Throwable t) {

            }
        });
    }


}//boardFragment
