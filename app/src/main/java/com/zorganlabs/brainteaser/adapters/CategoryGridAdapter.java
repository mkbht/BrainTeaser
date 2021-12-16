package com.zorganlabs.brainteaser.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.zorganlabs.brainteaser.R;
import com.zorganlabs.brainteaser.models.QuizCategory;

import java.util.List;
import java.util.Random;

public class CategoryGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<QuizCategory> categories;

    public CategoryGridAdapter(List<QuizCategory> list, Context context) {
        super();
        categories = list;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public CardView parentCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            parentCardView = (CardView) itemView.findViewById(R.id.parentCardView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_card, parent, false);
        return new ViewHolder(v);
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
}
