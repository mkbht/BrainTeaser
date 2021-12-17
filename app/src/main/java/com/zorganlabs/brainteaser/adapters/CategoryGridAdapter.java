package com.zorganlabs.brainteaser.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zorganlabs.brainteaser.R;
import com.zorganlabs.brainteaser.models.QuizCategory;

import java.util.List;

public class CategoryGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<QuizCategory> categories;
    private CategoryClickListener categoryClickListener;
    Context context;

    public CategoryGridAdapter(List<QuizCategory> list, CategoryClickListener listener) {
        super();
        // set list
        categories = list;
        categoryClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // member variables
        public TextView title;
        public CardView parentCardView;
        CategoryClickListener categoryClickListener;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView, CategoryClickListener categoryClickListener) {
            super(itemView);
            // fetch elements by id
            title = itemView.findViewById(R.id.title);
            parentCardView = itemView.findViewById(R.id.parentCardView);
            imageView = itemView.findViewById(R.id.image);
            this.categoryClickListener = categoryClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // get position on click of category
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
        // fetch category position
        QuizCategory quizCategory = categories.get(position);
        // set title to the view
        ((ViewHolder) holder).title.setText(quizCategory.getTitle());
        // set image to the view
        Glide.with(((ViewHolder) holder).imageView.getContext())
                .load(quizCategory.getImage())
                .into(((ViewHolder) holder).imageView);
    }

    @Override
    public int getItemCount() {
        // get categories size
        return categories.size();
    }

    public interface CategoryClickListener {
        // attach click listener
        void onCategoryClick(View view, int position);
    }
}
