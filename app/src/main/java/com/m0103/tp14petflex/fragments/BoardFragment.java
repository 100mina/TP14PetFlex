package com.m0103.tp14petflex.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.m0103.tp14petflex.adapters.BoardRecyclerAdapter;
import com.m0103.tp14petflex.databinding.FragmentBoardBinding;

public class BoardFragment extends Fragment {
    FragmentBoardBinding binding;
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
        adapter= new BoardRecyclerAdapter(getActivity());
        binding.recyclerViewBoard.setAdapter(adapter);
    }
}
