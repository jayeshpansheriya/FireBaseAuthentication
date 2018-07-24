package com.example.vnurture_android.firebasedemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ImageViewHolder>{
  Context context;
  List<Upload> mUploads;
  onItemClickListener mlistener;

    public ImageListAdapter(Context context, List<Upload> mUploads) {
        this.context = context;
        this.mUploads = mUploads;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.image_item,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
                Upload upload=mUploads.get(position);
                holder.textView.setText(upload.getmName());
        Picasso.with(context)
                .load(upload.getmImageurl())
                //.placeholder(R.drawable.jp)
                .fit()
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener{
        ImageView imageView;
        TextView textView;
        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.imageView);
            textView=(TextView)itemView.findViewById(R.id.name);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mlistener != null){
                int position=getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    mlistener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem doWhatever=menu.add(menu.NONE,1,1,"Do whatever");
            MenuItem delete=menu.add(menu.NONE,2,2,"Delete");

            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mlistener != null){
                int position=getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    switch (item.getItemId()){

                        case 1:
                            mlistener.onWhatEverClick(position);
                            return true;

                        case 2:
                            mlistener.onDeleteClick(position);
                            return true;

                    }
                }
            }
            return false;
        }
    }

    public interface onItemClickListener{
        void onItemClick(int position);
        void onWhatEverClick(int position);
        void onDeleteClick(int position);
    }

    public void setonItemClickListener(onItemClickListener listener){
        mlistener=listener;
    }
}
