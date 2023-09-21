package com.m0103.tp14petflex.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.m0103.tp14petflex.R;
import com.m0103.tp14petflex.databinding.FragmentUploadBinding;

public class UploadFragment extends Fragment {

    FragmentUploadBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentUploadBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO: 서버 전송 후 사진첨부 체크표시 visible, 회원만 업로드 가능.. if문을 어따걸어?
    }
}
