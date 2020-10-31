package com.example.dailytasks;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class RecyclerItem implements Parcelable{

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<RecyclerItem> CREATOR = new Parcelable.Creator<RecyclerItem>() {
        @Override
        public RecyclerItem createFromParcel(Parcel in) {
            return new RecyclerItem(in);
        }

        @Override
        public RecyclerItem[] newArray(int size) {
            return new RecyclerItem[size];
        }
    };
    private String title;
    private String description;

    public RecyclerItem(String title, String description) {
        this.title = title;
        this.description = description;
    }
    protected RecyclerItem(Parcel in) {
        title = in.readString();
        description = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}