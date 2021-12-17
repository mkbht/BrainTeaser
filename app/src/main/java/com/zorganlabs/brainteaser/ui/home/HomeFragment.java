package com.zorganlabs.brainteaser.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zorganlabs.brainteaser.LoginActivity;
import com.zorganlabs.brainteaser.R;
import com.zorganlabs.brainteaser.databinding.FragmentHomeBinding;
import com.zorganlabs.brainteaser.ui.explore.ExploreFragment;

import org.w3c.dom.Text;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final TextView homePageUserName = binding.homePageUserName;
        final TextView txtRewardPoints = binding.rewardValueHome;
        final TextView rankValue = binding.rankValue;

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            homePageUserName.setText(currentUser.getDisplayName());
        } else {
            homePageUserName.setText("Visitor!");
        }

        Button submit = root.findViewById(R.id.startQuiz);

        // fetch reward points
        SharedPreferences sharedPref = getActivity().getSharedPreferences("scores", Context.MODE_PRIVATE);
        int rewardPoints = sharedPref.getInt("REWARD_POINTS", 0);
        int correct = sharedPref.getInt("CORRECT", 0) == 0 ? 1 : sharedPref.getInt("CORRECT", 0);
        int mistakes = sharedPref.getInt("MISTAKES", 0) == 0 ? 1 : sharedPref.getInt("MISTAKES", 0);

        String ratio = String.format("%.2f",
                (double) correct / mistakes);
        txtRewardPoints.setText(String.valueOf(rewardPoints));
        rankValue.setText(ratio);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(root).navigate(R.id.navigation_explore);
            }
        });

        final Button logoutButton = binding.logOut;

        logoutButton.setOnClickListener(view -> {
            mAuth.signOut();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}