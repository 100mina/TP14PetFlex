package com.m0103.tp14petflex.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.m0103.tp14petflex.R;
import com.m0103.tp14petflex.activities.PlaceUrlActivity;
import com.m0103.tp14petflex.data.Place;
import com.m0103.tp14petflex.databinding.RecyclerPlaceBinding;

import java.util.ArrayList;

public class PlaceRecyclerAdapter extends RecyclerView.Adapter<PlaceRecyclerAdapter.VH> {
    Context context;
    ArrayList<Place> places;

    public PlaceRecyclerAdapter(Context context, ArrayList<Place> places) {
        this.context = context;
        this.places = places;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.recycler_place,parent,false);
        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Place place= places.get(position);

        holder.binding.placeTvName.setText(place.place_name);

        if(place.road_address_name.equals("")) holder.binding.placeTvAddress.setText(place.address_name);
        else holder.binding.placeTvAddress.setText(place.road_address_name);

        holder.binding.placeTvDistance.setText(place.distance+"m");

        if(place.phone.equals("")) holder.binding.placeTvPhone.setText("(매장 번호 정보가 없어요\uD83D\uDE22)");
        else holder.binding.placeTvPhone.setText(place.phone);

        holder.itemView.setOnClickListener(view -> {
            Intent intent=new Intent(context, PlaceUrlActivity.class);
            intent.putExtra("placeUrl",place.place_url);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    class VH extends RecyclerView.ViewHolder{
        RecyclerPlaceBinding binding;

        public VH(@NonNull View itemView) {
            super(itemView);
            binding=RecyclerPlaceBinding.bind(itemView);
        }
    }
}
