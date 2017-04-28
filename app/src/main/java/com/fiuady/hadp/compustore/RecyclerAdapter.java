package com.fiuady.hadp.compustore;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jessm on 28/04/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private String[] titles = {"Enero",
            "Febrero",
            "Marzo",
            "Abril",
            "Mayo",
            "Junio",
            "Julio",
            "Agosto",
    "Septiembre",
    "Octubre",
    "Noviembre",
    "Diciembre"};

    private String[] details = {"Total:" + " ",
            "Total:" + " ","Total:" + " ","Total:" + " ","Total:" + " ","Total:" + " ",
            "Total:" + " ","Total:" + " ","Total:" + " ","Total:" + " ","Total:" + " ",
            "Total:" + " "};

    private int[] images = { R.drawable.calendar,
            R.drawable.calendar,
            R.drawable.calendar,
            R.drawable.calendar,
            R.drawable.calendar,
            R.drawable.calendar,
            R.drawable.calendar,
            R.drawable.calendar,
            R.drawable.calendar,
            R.drawable.calendar,
            R.drawable.calendar,
            R.drawable.calendar };

    class ViewHolder extends RecyclerView.ViewHolder{

        public int currentItem;
        public ImageView itemImage;
        public TextView itemTitle;
        public TextView itemDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView)itemView.findViewById(R.id.item_image);
            itemTitle = (TextView)itemView.findViewById(R.id.item_title);
            itemDetail =
                    (TextView)itemView.findViewById(R.id.item_detail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();

                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemTitle.setText(titles[i]);
        viewHolder.itemDetail.setText(details[i]);
        viewHolder.itemImage.setImageResource(images[i]);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
