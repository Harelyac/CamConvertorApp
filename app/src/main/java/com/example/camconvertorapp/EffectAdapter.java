package com.example.camconvertorapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



public class EffectAdapter extends RecyclerView.Adapter<EffectAdapter.MyViewHolder>  {
    interface AdapterClickCallback{
        void AdapterClickCallback(String freq[], int position, View view);
    }

    public AdapterClickCallback callback;


    String frequencies[];
    private int images[];

    LayoutInflater inflter;
    public EffectAdapter( String[] frequenciesyList, int[] images) {
        this.frequencies = frequenciesyList;
        this.images = images;
//        inflter = (LayoutInflater.from(applicationContext));
    }
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        public ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.sample_icon);
            textView = itemView.findViewById(R.id.list_item_text);
        }

    }

        // Create new views (invoked by the layout manager)
        @NonNull
        @Override
        public EffectAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            // create a new view
            View view =  LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item,
                    viewGroup, false);
            view.setTag(Techniques.values()[i]);
            final MyViewHolder holder = new MyViewHolder(view);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(callback != null) {
                        callback.AdapterClickCallback(frequencies, holder.getAdapterPosition(),v);
                    }

                }
            });

            return holder;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder chatHolder, int i) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            chatHolder.textView.setText(frequencies[i]);
            chatHolder.imageView.setImageResource(images[i]);


        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return frequencies.length;
        }







//PROBLEM: tried with ListView - problem with inflating the layout -->


//    @Override
//    public int getCount() {
//        return frequencies.length;
//
//    }
//    @Override
//    public Object getItem(int position) {
//
//        return Techniques.values()[position].getAnimator();
//    }
//
//    @Override
//    public long getItemId(int position) {
//
//        return position;
//    }
//
//
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//
//        View v = inflter.inflate(R.layout.item,null);
//        TextView t = (TextView)v.findViewById(R.id.list_item_text);
//        ImageView im = (ImageView) v.findViewById(R.id.sample_icon);
////        Object o = getItem(position);
////        int start = o.getClass().getName().lastIndexOf(".") + 1;
////        String name = o.getClass().getName().substring(start);
//        im.setImageResource(images[position]);
//        t.setText(frequencies[position]);
//        v.setTag(Techniques.values()[position]);
//        return v;
//    }

}