package com.m0103.tp14petflex.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.m0103.tp14petflex.R;
import com.m0103.tp14petflex.databinding.RecyclerHomeBinding;
import com.m0103.tp14petflex.databinding.RecyclerHomeFirstBinding;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;

    public HomeRecyclerAdapter(Context context) {
        this.context = context;
    }
    int i=0;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(i==0){
            View itemview=LayoutInflater.from(context).inflate(R.layout.recycler_home_first,parent,false);
            i=1;
            return new VH_first(itemview);
        }else {
            View itemview=LayoutInflater.from(context).inflate(R.layout.recycler_home,parent,false);
            return new VH(itemview);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }


    class VH_first extends RecyclerView.ViewHolder{
        RecyclerHomeFirstBinding binding;
        public VH_first(@NonNull View itemView) {
            super(itemView);
            binding=RecyclerHomeFirstBinding.bind(itemView);
        }
    }

    class VH extends RecyclerView.ViewHolder{
        RecyclerHomeBinding binding;
        public VH(@NonNull View itemView) {
            super(itemView);
            binding=RecyclerHomeBinding.bind(itemView);
        }
    }
}
