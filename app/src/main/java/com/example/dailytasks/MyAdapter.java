package com.example.dailytasks;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.Selection;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.dailytasks.ui.home.HomeFragment;
import com.example.dailytasks.ui.home.ItemMoveCallback;
import com.example.dailytasks.ui.home.StartDragListener;

import java.util.Collections;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {
    private List<RecyclerItem> listItems;
    private StartDragListener mStartDragListener;
    private final ItemClickListener itemClicklistener;

    public MyAdapter(List<RecyclerItem> listItems,StartDragListener startDragListener, ItemClickListener itemClicklistener) {
        this.listItems = listItems;
        mStartDragListener = startDragListener;
        this.itemClicklistener = itemClicklistener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
            RecyclerView.ViewHolder h = new RecyclerView.ViewHolder(v) {};
            return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final RecyclerItem itemList = listItems.get(position);
        holder.txtTitle.setText(itemList.getTitle());
        //holder.txtDescription.setText(itemList.getDescription());
            ((ViewHolder) holder).imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() ==
                        MotionEvent.ACTION_DOWN) {
                    mStartDragListener.requestDrag(holder);
                }
                return false;
            }
        });
        ViewCompat.setTransitionName(holder.card, itemList.getTitle() + position);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClicklistener.onItemClick(holder.getAdapterPosition(), itemList, holder.card);
            }
        });

        }

    public void removeItem(int position) {
        listItems.remove(position);
        notifyItemRemoved(position);
    }

    public void refreshAdapter() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    //@Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(listItems, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(listItems, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    public List<RecyclerItem> getData() {
        return listItems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtTitle;
        public TextView txtDescription;
        public CardView card;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtDescription = (TextView) itemView.findViewById(R.id.txtDescription);
            imageView = itemView.findViewById(R.id.imageView);
            card = itemView.findViewById(R.id.card);




        }

    }


}
