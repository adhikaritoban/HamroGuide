package com.example.sajak.hamroguide.news;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sajak.hamroguide.R;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.RecyclerViewHolder>{

    Context mContext;
    ArrayList<NewsGetSet> arrayList = new ArrayList<>();

    NewsAdapter(Context context, ArrayList<NewsGetSet> arrayList){
        this.arrayList = arrayList;
        this.mContext = context;
    }


    @NonNull
    @Override
    public NewsAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_list,viewGroup,false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.RecyclerViewHolder recyclerViewHolder, int i) {

        NewsGetSet newsGetSet = arrayList.get(i);

        recyclerViewHolder.id.setText(newsGetSet.getId());
        recyclerViewHolder.head.setText(newsGetSet.getHead());
        recyclerViewHolder.date.setText(newsGetSet.getDate());
        recyclerViewHolder.web.setText(newsGetSet.getWebsite());
        recyclerViewHolder.content.setText(newsGetSet.getContent());
        recyclerViewHolder.writer.setText(newsGetSet.getWriter());

//        Glide.with(mContext).load(newsGetSet.getImage()).into(recyclerViewHolder.imagef);
//        Glide.with(mContext).load(newsGetSet.getImagesec()).into(recyclerViewHolder.images);
//        Glide.with(mContext).load(newsGetSet.getImagethird()).into(recyclerViewHolder.imaget);


        Glide.with(mContext).load(newsGetSet.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .into(recyclerViewHolder.imagef);

        Glide.with(mContext).load(newsGetSet.getImagesec())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .into(recyclerViewHolder.images);

        Glide.with(mContext).load(newsGetSet.getImagethird())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .into(recyclerViewHolder.imaget);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView id, head, content, date, writer, web, readmore;
        ImageView imagef, images, imaget;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.newsId);
            head = itemView.findViewById(R.id.newsHead);
            content = itemView.findViewById(R.id.newsContent);
            date = itemView.findViewById(R.id.newsDate);
            writer = itemView.findViewById(R.id.newsWriter);
            web = itemView.findViewById(R.id.newsWeb);

            imagef = itemView.findViewById(R.id.imagefirst);
            images = itemView.findViewById(R.id.imagesecond);
            imaget = itemView.findViewById(R.id.imagethird);

            readmore = itemView.findViewById(R.id.newsReadmore);

            readmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = web.getText().toString();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    view.getContext().startActivity(i);
                }
            });
        }
    }
}
