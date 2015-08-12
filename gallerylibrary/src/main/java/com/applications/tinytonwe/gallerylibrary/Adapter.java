package com.applications.tinytonwe.gallerylibrary;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by admin on 8/3/2015.
 */
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private Bitmap [] images_;
    private static RecyclerViewClickListener itemListener_;

    public Adapter(Bitmap[] images, RecyclerViewClickListener itemListener) {
        images_ = images.clone();
        itemListener_ = itemListener;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imageView_;

        public ViewHolder(View v) {
            super(v);
            imageView_ = (ImageView)v.findViewById(R.id.thumbnail);
            imageView_.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            itemListener_.recyclerViewListClicked(this.getLayoutPosition());

        }

    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_thumbnail, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imageView_.setImageBitmap(images_[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return images_.length;
    }

}
