package com.zorganlabs.brainteaser.ui.explore;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zorganlabs.brainteaser.QuizActivity;
import com.zorganlabs.brainteaser.adapters.CategoryGridAdapter;
import com.zorganlabs.brainteaser.databinding.FragmentExploreBinding;
import com.zorganlabs.brainteaser.models.QuizCategory;

import java.util.ArrayList;

public class ExploreFragment extends Fragment implements CategoryGridAdapter.CategoryClickListener {
    FragmentExploreBinding binding;
    RecyclerView categoryList;
    CategoryGridAdapter categoryGridAdapter;
    ArrayList<QuizCategory> categories = new ArrayList<QuizCategory>();
    DatabaseReference quizRef;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentExploreBinding.inflate(inflater, container, false);
        categoryList = binding.categoryList;
        View root = binding.getRoot();
        quizRef = FirebaseDatabase.getInstance().getReference().child("quiz");

        // fetch the categories
        fetchCategories();

        // bind categories with adapter
        bindCategoryAdapter();

        return root;
    }

    private void fetchCategories() {
        quizRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot quizSnapShot: snapshot.getChildren()) {
                    // get value from QuizCategory class
                    QuizCategory category = quizSnapShot.getValue(QuizCategory.class);
                    // set title and images
                    categories.add(new QuizCategory(category.getTitle(), category.getImage()));
                }
                // notify changes
                categoryGridAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void bindCategoryAdapter() {
        // bind data with the adapter
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        categoryList.setLayoutManager(layoutManager);
        categoryGridAdapter = new CategoryGridAdapter(categories, this);
        categoryList.setAdapter(categoryGridAdapter);
    }

    @Override
    public void onCategoryClick(View view, int position) {
        // move to QuizActivity class on click of category
        Intent intent = new Intent(getContext(), QuizActivity.class);
        // pass category name to the activity
        intent.putExtra("CATEGORY_NAME", categories.get(position).getTitle());
        startActivity(intent);
    }
}