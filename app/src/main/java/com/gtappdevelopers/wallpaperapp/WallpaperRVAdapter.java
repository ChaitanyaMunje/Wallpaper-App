package com.gtappdevelopers.wallpaperapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class WallpaperRVAdapter extends RecyclerView.Adapter<WallpaperRVAdapter.ViewHolder> {
    //creating variables for array list and context.
    private ArrayList<String> wallPaperList;
    private Context context;

    //creating a constructor.
    public WallpaperRVAdapter(ArrayList<String> wallPaperList, Context context) {
        this.wallPaperList = wallPaperList;
        this.context = context;
    }

    @NonNull
    @Override
    public WallpaperRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating our layout file on below line.
        View view = LayoutInflater.from(context).inflate(R.layout.wallpaper_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WallpaperRVAdapter.ViewHolder holder, int position) {
        //setting data to all our views.
        Glide.with(context).load(wallPaperList.get(position)).into(holder.wallpaperIV);
        //adding on click listener to item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //passing data through intent on below line.
                Intent i = new Intent(context, WallpaperActivity.class);
                i.putExtra("imgUrl", wallPaperList.get(position));
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return wallPaperList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //creating variables for our views which are created in layout file.
        private CardView imageCV;
        private ImageView wallpaperIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //initializing all the variables.
            wallpaperIV = itemView.findViewById(R.id.idIVWallpaper);
            imageCV = itemView.findViewById(R.id.idCVWallpaper);

        }
    }
}
