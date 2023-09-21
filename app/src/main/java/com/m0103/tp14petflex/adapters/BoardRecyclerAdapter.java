package com.m0103.tp14petflex.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.m0103.tp14petflex.R;
import com.m0103.tp14petflex.databinding.RecyclerBoardBinding;

public class BoardRecyclerAdapter extends RecyclerView.Adapter<BoardRecyclerAdapter.VH> {
    Context context;

    public BoardRecyclerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.recycler_board,parent,false);
        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 30;
    }

    class VH extends RecyclerView.ViewHolder{
        RecyclerBoardBinding binding;
        public VH(@NonNull View itemView) {
            super(itemView);
            binding=RecyclerBoardBinding.bind(itemView);
        }
    }
}
