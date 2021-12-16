package com.zorganlabs.brainteaser.ui.explore;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zorganlabs.brainteaser.adapters.CategoryGridAdapter;
import com.zorganlabs.brainteaser.databinding.FragmentExploreBinding;
import com.zorganlabs.brainteaser.models.QuizCategory;

import java.util.ArrayList;

public class ExploreFragment extends Fragment {


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

        fetchCategories();

        bindCategoryAdapter();

        return root;
    }

    private void fetchCategories() {
        quizRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot quizSnapShot: snapshot.getChildren()) {

                    QuizCategory category = quizSnapShot.getValue(QuizCategory.class);
                    categories.add(new QuizCategory(category.getTitle()));
                }
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
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        categoryList.setLayoutManager(layoutManager);
        categoryGridAdapter = new CategoryGridAdapter(categories, getContext());
        categoryList.setAdapter(categoryGridAdapter);
    }
}