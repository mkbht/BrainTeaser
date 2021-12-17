package com.zorganlabs.brainteaser.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.zorganlabs.brainteaser.R;
import com.zorganlabs.brainteaser.models.QuizCategory;

import java.net.URL;
import java.util.List;
import java.util.Random;

public class CategoryGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<QuizCategory> categories;
    private CategoryClickListener categoryClickListener;
    Context context;

    public CategoryGridAdapter(List<QuizCategory> list, CategoryClickListener listener) {
        super();
        categories = list;
        categoryClickListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title;
        public CardView parentCardView;
        CategoryClickListener categoryClickListener;

        public ViewHolder(@NonNull View itemView, CategoryClickListener categoryClickListener) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            parentCardView = (CardView) itemView.findViewById(R.id.parentCardView);
            this.categoryClickListener = categoryClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            categoryClickListener.onCategoryClick(view, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_card, parent, false);
        context = parent.getContext();
        return new ViewHolder(v, categoryClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        QuizCategory quizCategory = categories.get(position);
        ((ViewHolder) holder).title.setText(quizCategory.getTitle());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public interface CategoryClickListener {
        void onCategoryClick(View view, int position);
    }
}
