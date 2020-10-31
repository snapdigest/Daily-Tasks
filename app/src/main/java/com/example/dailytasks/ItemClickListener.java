package com.example.dailytasks;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

public interface ItemClickListener {
    void onItemClick(int pos, RecyclerItem itemList, CardView shareItemView);
}
