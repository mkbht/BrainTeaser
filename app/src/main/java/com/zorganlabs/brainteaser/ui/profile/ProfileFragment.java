package com.zorganlabs.brainteaser.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zorganlabs.brainteaser.HomeActivity;
import com.zorganlabs.brainteaser.LoginActivity;
import com.zorganlabs.brainteaser.databinding.FragmentProfileBinding;
import com.zorganlabs.brainteaser.ui.home.HomeFragment;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView displayName = binding.displayName;
        final Button logoutButton = binding.logoutButton;

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            displayName.setText(currentUser.getDisplayName());
        } else {
            displayName.setText("");
        }

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