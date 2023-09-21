package com.m0103.tp14petflex.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.m0103.tp14petflex.adapters.HomeRecyclerAdapter;
import com.m0103.tp14petflex.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentHomeBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //RecyclerView <-> Adapter 연결 / GridLayout 속성 추가
        HomeRecyclerAdapter adapter=new HomeRecyclerAdapter(getActivity());
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

        // TODO: 나도 자랑하러 가기 버튼 -> 자랑하기 탭
    }
}
