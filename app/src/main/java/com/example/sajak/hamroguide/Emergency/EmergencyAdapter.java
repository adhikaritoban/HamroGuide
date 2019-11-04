package com.example.sajak.hamroguide.Emergency;

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

import com.example.sajak.hamroguide.R;

import java.util.ArrayList;

public class EmergencyAdapter extends RecyclerView.Adapter<EmergencyAdapter.RecyclerViewHolder>{

    Context mContext;
    ArrayList<EmergencyGetSet> arrayList = new ArrayList<>();
    private int PHONE_PERMISSION_CODE = 03;

    EmergencyAdapter(Context context, ArrayList<EmergencyGetSet> arrayList){
        this.mContext = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public EmergencyAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.emergency_list,viewGroup,false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EmergencyAdapter.RecyclerViewHolder recyclerViewHolder, int i) {
        EmergencyGetSet emergencyGetSet = arrayList.get(i);
        String num = String.valueOf(emergencyGetSet.getNumber());
        recyclerViewHolder.number.setText(num);
        recyclerViewHolder.name.setText(emergencyGetSet.getName());

        recyclerViewHolder.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + num));
                        mContext.startActivity(callIntent);
                    }else{
                        if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.CALL_PHONE)){
                            Toast.makeText(mContext, "App requires Phone Call permission.\nPlease allow that in the device settings.", Toast.LENGTH_LONG).show();
                        }
                        ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CALL_PHONE}, PHONE_PERMISSION_CODE);
                    }
                }else{
                    mContext.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + num)));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void setFilter(ArrayList<EmergencyGetSet> newList) {
        arrayList = new ArrayList<>();
        arrayList.addAll(newList);
        notifyDataSetChanged(); //refresh the adapter
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView number, name;
        ImageView phone;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phoneCall);
        }
    }
}
