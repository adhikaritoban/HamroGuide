package com.example.sajak.hamroguide.Agencies;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sajak.hamroguide.MainActivity;
import com.example.sajak.hamroguide.MyLocation;
import com.example.sajak.hamroguide.R;

import java.util.ArrayList;

public class AgenciesAdapter extends RecyclerView.Adapter<AgenciesAdapter.RecyclerViewHolder>{

    Context mContext;
    ArrayList<AgenciesGetSet> arrayList = new ArrayList<>();
    private final static int REQUEST_READ_SMS_PERMISSION = 3004;
    private int PHONE_PERMISSION_CODE = 03;

    AgenciesAdapter(Context context, ArrayList<AgenciesGetSet> arrayList){
        this.arrayList = arrayList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public AgenciesAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.agencies_list,viewGroup,false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AgenciesAdapter.RecyclerViewHolder recyclerViewHolder, int i) {
        AgenciesGetSet agenciesGetSet = arrayList.get(i);
        recyclerViewHolder.id.setText(agenciesGetSet.getId());
        recyclerViewHolder.name.setText(agenciesGetSet.getName());
        recyclerViewHolder.contact.setText(agenciesGetSet.getContact());
        recyclerViewHolder.address.setText(agenciesGetSet.getLocation());
        recyclerViewHolder.latitude.setText(agenciesGetSet.getLatitude());
        recyclerViewHolder.longitude.setText(agenciesGetSet.getLongitude());
        Glide.with(mContext).load(agenciesGetSet.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .into(recyclerViewHolder.image);

//        Glide.with(mContext).load(agenciesGetSet.getImage()).into(recyclerViewHolder.image);

        recyclerViewHolder.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + agenciesGetSet.getContact()));
                        mContext.startActivity(callIntent);
                    }else{
                        if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.CALL_PHONE)){
                            Toast.makeText(mContext, "App requires Phone Call permission.\nPlease allow that in the device settings.", Toast.LENGTH_LONG).show();
                        }
                        ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CALL_PHONE}, PHONE_PERMISSION_CODE);
                    }
                }else{
                    mContext.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + agenciesGetSet.getContact())));
                }
            }
        });
        recyclerViewHolder.sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext,"SMS",Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED){
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + agenciesGetSet.getContact()));
                        intent.putExtra("sms_body", "Hello "+ agenciesGetSet.getName() + ", ");
                        mContext.startActivity(intent);

                    }else{
                        if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.READ_SMS)){
                            Toast.makeText(mContext, "App requires SMS Read permission.\nPlease allow that in the device settings.", Toast.LENGTH_LONG).show();
                        }
                        ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_SMS}, REQUEST_READ_SMS_PERMISSION);
                    }
                }else{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + agenciesGetSet.getContact()));
                    intent.putExtra("sms_body", "Hello "+ agenciesGetSet.getName() + ", ");
                    mContext.startActivity(intent);
                }
            }
        });

        recyclerViewHolder.agencyNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String from = "Agencies";
                Intent intent = new Intent(mContext, MyLocation.class);
                intent.putExtra("from", from);
                Double a_lat = Double.parseDouble(agenciesGetSet.getLatitude());
                Double a_lon = Double.parseDouble(agenciesGetSet.getLongitude());
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

    public void setFilter(ArrayList<AgenciesGetSet> newList) {
        arrayList = new ArrayList<>();
        arrayList.addAll(newList);
        notifyDataSetChanged(); //refresh the adapter
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView name, address, contact, latitude, longitude, id;
        ImageView image, phone, sms, agencyNav;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.agenciesId);
            name = itemView.findViewById(R.id.agenciesName);
            address = itemView.findViewById(R.id.agenciesAddress);
            contact = itemView.findViewById(R.id.agenciesCon);
            latitude = itemView.findViewById(R.id.agenciesLat);
            longitude = itemView.findViewById(R.id.agenciesLon);
            image = itemView.findViewById(R.id.agenciesImage);
            phone = itemView.findViewById(R.id.agenciesPhone);
            sms = itemView.findViewById(R.id.agenciesSms);
            agencyNav = itemView.findViewById(R.id.navAgencies);
        }
    }
}
