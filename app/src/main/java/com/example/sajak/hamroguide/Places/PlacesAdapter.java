package com.example.sajak.hamroguide.Places;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sajak.hamroguide.MyLocation;
import com.example.sajak.hamroguide.R;

import java.util.ArrayList;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.RecyclerViewHolder>{

    Context mContext;
    ArrayList<PlacesGetSet> arrayList = new ArrayList<>();

    PlacesAdapter(Context context, ArrayList<PlacesGetSet> arrayList){
        this.mContext = context;
        this.arrayList = arrayList;
        Log.d("Here", "adapter called");
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.places_list,viewGroup,false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);

        Log.e("Check", "Here");
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int i) {
        PlacesGetSet placesGetSet = arrayList.get(i);
        recyclerViewHolder.id.setText(placesGetSet.getId());
        recyclerViewHolder.lat.setText(String.valueOf(placesGetSet.getLatitude()));
        recyclerViewHolder.lon.setText(String.valueOf(placesGetSet.getLongitude()));
        recyclerViewHolder.location.setText(placesGetSet.getLocation());
        recyclerViewHolder.place.setText(placesGetSet.getPlace());

        Log.e("Check", "onBind" + placesGetSet.getPlace());

        Glide.with(mContext).load(placesGetSet.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.hamroguidelogo)
                .into(recyclerViewHolder.img);

        if (placesGetSet.getId().equals("p1")){
            recyclerViewHolder.placedesc.setText(R.string.p1);
        }
        else if (placesGetSet.getId().equals("p2")){
            recyclerViewHolder.placedesc.setText(R.string.p2);
        }

        else if (placesGetSet.getId().equals("p3")){
            recyclerViewHolder.placedesc.setText(R.string.p3);
        }

        else if (placesGetSet.getId().equals("p4")){
            recyclerViewHolder.placedesc.setText(R.string.p4);
        }

        else if (placesGetSet.getId().equals("p5")){
            recyclerViewHolder.placedesc.setText(R.string.p5);
        }

        else{
            recyclerViewHolder.placedesc.setText(R.string.nothing);
        }


        recyclerViewHolder.map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String from = "Places";
                Intent intent = new Intent(mContext, MyLocation.class);
                intent.putExtra("from", from);
                Double a_lat = placesGetSet.getLatitude();
                Double a_lon = placesGetSet.getLongitude();
                intent.putExtra("a_lat",a_lat);
                intent.putExtra("a_lon",a_lon);
                intent.putExtra("from",from);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void setFilter(ArrayList<PlacesGetSet> newList) {
        arrayList = new ArrayList<>();
        arrayList.addAll(newList);
        notifyDataSetChanged(); //refresh the adapter
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView id, lat, lon, location, place, placedesc;
        ImageView img, map;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.placesId);
            lat = itemView.findViewById(R.id.placesLat);
            lon = itemView.findViewById(R.id.placesLon);
            location = itemView.findViewById(R.id.placeAddress);
            place = itemView.findViewById(R.id.placeName);
            placedesc = itemView.findViewById(R.id.placeDesc);
            img = itemView.findViewById(R.id.placeImg);
            map = itemView.findViewById(R.id.navPlaces);
        }
    }
}
