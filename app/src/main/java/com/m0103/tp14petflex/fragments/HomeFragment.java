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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.m0103.tp14petflex.G;
import com.m0103.tp14petflex.activities.MainActivity;
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
    //MainActivity ma= (MainActivity) getActivity();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentHomeBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Retrofit retrofit = RetrofitHelper.getRetrofitInstance("http://petflex.dothome.co.kr/");
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);

        //기본으로 보여줄 데이터 가져오기 (전달의 순위)
        Call<ArrayList<BoardData>> call = retrofitService.loadRank(G.start.toString(), G.end.toString());
        call.enqueue(new Callback<ArrayList<BoardData>>() {
            @Override
            public void onResponse(Call<ArrayList<BoardData>> call, Response<ArrayList<BoardData>> response) {
                ArrayList<BoardData> items = response.body();
                for (int i = 0; i < items.size(); i++) {
                    BoardData item = items.get(i);
                    boardDataArrayList.add(item);
                }
                recyclerViewSetAdapter("lastMonth");
            }

            @Override
            public void onFailure(Call<ArrayList<BoardData>> call, Throwable t) {
            }
        });

        //스크롤에 따라 버튼 보이기
        binding.recyclerViewHome.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;
                if (lastVisibleItemPosition == itemTotalCount) {
                    binding.homeBtnTotalFav.setVisibility(View.VISIBLE);
                    binding.homeBtnLastWeek.setVisibility(View.VISIBLE);
                }
                if (lastVisibleItemPosition == itemTotalCount-1) {
                    binding.homeBtnTotalFav.setVisibility(View.GONE);
                    binding.homeBtnLastWeek.setVisibility(View.GONE);
                }
            }
        });

        binding.homeBtnTotalFav.setOnClickListener(view1 -> {
            //총 좋아요 순위
            Call<ArrayList<BoardData>> call1 = retrofitService.loadTotalFav();
            call1.enqueue(new Callback<ArrayList<BoardData>>() {
                @Override
                public void onResponse(Call<ArrayList<BoardData>> call, Response<ArrayList<BoardData>> response) {
                    boardDataArrayList.clear();

                    ArrayList<BoardData> items = response.body();
                    for (int i = 0; i < 10; i++) {
                        BoardData item = items.get(i);
                        boardDataArrayList.add(item);
                    }
                    recyclerViewSetAdapter("totalFav");
                    MainActivity.binding.mainToolbar.setTitle("역대 최고 사랑둥이들");
                    binding.homeBtnTotalFav.setVisibility(View.GONE);
                    binding.homeBtnLastWeek.setVisibility(View.GONE);

                }
                @Override
                public void onFailure(Call<ArrayList<BoardData>> call, Throwable t) {
                }
            });

        });

        binding.homeBtnLastWeek.setOnClickListener(view1 -> {
            //지난 일주일간의 순위
            Call<ArrayList<BoardData>> call1 = retrofitService.loadLastWeek();
            call1.enqueue(new Callback<ArrayList<BoardData>>() {
                @Override
                public void onResponse(Call<ArrayList<BoardData>> call, Response<ArrayList<BoardData>> response) {
                    boardDataArrayList.clear();

                    ArrayList<BoardData> items = response.body();
                    for (int i = 0; i < items.size(); i++) {
                        BoardData item = items.get(i);
                        boardDataArrayList.add(item);
                    }
                    recyclerViewSetAdapter("lastWeek");
                    MainActivity.binding.mainToolbar.setTitle("일주일간의 최고 사랑둥이들");
                    binding.homeBtnTotalFav.setVisibility(View.GONE);
                    binding.homeBtnLastWeek.setVisibility(View.GONE);
                }
                @Override
                public void onFailure(Call<ArrayList<BoardData>> call, Throwable t) {
                }
            });
        });

    }

    void recyclerViewSetAdapter(String type){

        //RecyclerView <-> Adapter 연결 / GridLayout 속성 추가
        HomeRecyclerAdapter adapter = new HomeRecyclerAdapter(getActivity(), boardDataArrayList, type);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) return 2; //return 값 : 아이템이 먹을 칸 수, 부피
                else return 1;
            }
        });
        binding.recyclerViewHome.setLayoutManager(layoutManager);
        binding.recyclerViewHome.setAdapter(adapter);
    }




}
