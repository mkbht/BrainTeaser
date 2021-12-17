package com.zorganlabs.brainteaser.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.zorganlabs.brainteaser.R;
import com.zorganlabs.brainteaser.models.Leaderboard;
import com.zorganlabs.brainteaser.models.QuizCategory;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Leaderboard> leaderboards;

    public LeaderboardAdapter(List<Leaderboard> list) {
        super();
        leaderboards = list;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView category;
        public TextView correct;
        public TextView incorrect;
        public TextView ratio;
        public TextView total;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            category = (TextView) itemView.findViewById(R.id.category);
            correct = (TextView) itemView.findViewById(R.id.correct);
            incorrect = (TextView) itemView.findViewById(R.id.incorrect);
            incorrect = (TextView) itemView.findViewById(R.id.incorrect);
            total = (TextView) itemView.findViewById(R.id.total);
            ratio = (TextView) itemView.findViewById(R.id.ratio);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Leaderboard leaderboard = leaderboards.get(position);
        ((ViewHolder) holder).category.setText(leaderboard.getCategory());
        ((ViewHolder) holder).correct.setText("Correct: " + leaderboard.getCorrect());
        ((ViewHolder) holder).incorrect.setText("Incorrect: " + leaderboard.getIncorrect());
        ((ViewHolder) holder).total.setText("Games played: " + leaderboard.getTotal());
        String ciRatio = String.format("C/I Ratio: %.2f",
                (double) leaderboard.getCorrect() / (leaderboard.getIncorrect() != 0 ? leaderboard.getIncorrect() : 1));
        ((ViewHolder) holder).ratio.setText(ciRatio);
    }

    @Override
    public int getItemCount() {
        return leaderboards.size();
    }
}
