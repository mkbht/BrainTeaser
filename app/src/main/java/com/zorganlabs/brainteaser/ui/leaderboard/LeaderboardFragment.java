package com.zorganlabs.brainteaser.ui.leaderboard;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zorganlabs.brainteaser.DatabaseHandler;
import com.zorganlabs.brainteaser.LoginActivity;
import com.zorganlabs.brainteaser.adapters.LeaderboardAdapter;
import com.zorganlabs.brainteaser.databinding.FragmentLeaderboardBinding;
import com.zorganlabs.brainteaser.models.Leaderboard;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardFragment extends Fragment {

    FragmentLeaderboardBinding binding;
    DatabaseHandler databaseHandler;
    RecyclerView leaderboardRecyclerView;
    LeaderboardAdapter leaderboardAdapter;
    List<Leaderboard> leaderboards = new ArrayList<Leaderboard>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLeaderboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        leaderboardRecyclerView = binding.leaderboardRecyclerView;

        // retrieve leaderboard data
        databaseHandler = new DatabaseHandler(getActivity());

        Cursor cursor = databaseHandler.viewLeaderBoard();
        if(cursor.getCount() == 0) {
            Toast.makeText(getContext(), "No records found.", Toast.LENGTH_SHORT).show();
        } else {
            if(cursor.moveToFirst()) {
                do {
                    Leaderboard leaderboard = new Leaderboard();
                    leaderboard.setCategory(cursor.getString(cursor.getColumnIndexOrThrow("category")));
                    leaderboard.setCorrect(cursor.getInt(cursor.getColumnIndexOrThrow("correct")));
                    leaderboard.setIncorrect(cursor.getInt(cursor.getColumnIndexOrThrow("incorrect")));
                    leaderboard.setTotal(cursor.getInt(cursor.getColumnIndexOrThrow("total")));
                    leaderboards.add(leaderboard);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        // load data to recyclerview
        bindAdapter();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void bindAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        leaderboardRecyclerView.setLayoutManager(layoutManager);
        leaderboardAdapter = new LeaderboardAdapter(leaderboards);
        leaderboardRecyclerView.setAdapter(leaderboardAdapter);
        leaderboardAdapter.notifyDataSetChanged();
    }
}