package com.example.datapirates;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datapirates.model.Review;

import java.util.ArrayList;

public class reviewListAdapter extends RecyclerView.Adapter<reviewListAdapter.reviewListViewHolder>{

    Context context;
    ArrayList<Review> list;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onDelete(String reviewKey,String bookName, int position);
        void onUpdate(String reviewKey, String bookName, String description, String rating, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public reviewListAdapter(Context context, ArrayList<Review> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public reviewListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new reviewListViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull reviewListViewHolder holder, int position) {
        Review review = list.get(position);
        holder.reviewKey.setText(review.getReviewId());
        holder.bookName.setText(review.getBookName());
        holder.description.setText(review.getDescription());
        holder.rating.setRating(Float.parseFloat(review.getRating()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
