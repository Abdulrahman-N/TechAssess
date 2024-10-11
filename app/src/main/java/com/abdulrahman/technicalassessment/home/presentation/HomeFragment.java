package com.abdulrahman.technicalassessment.home.presentation;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.abdulrahman.technicalassessment.R;
import com.abdulrahman.technicalassessment.databinding.FragmentHomeBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private boolean insetsApplied = false;
    private HomeViewModel viewModel;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentHomeBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        setupView();
        setupObservers();
    }


    private void setupObservers() {
        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), error -> showMessage("Error", error));
        viewModel.getConnectionStatusLiveData().observe(getViewLifecycleOwner(), isConnected -> {
            if (isConnected) {
                binding.connectBtn.setText("Disconnect");
                binding.connectionStatus.setText("Connected to websocket!");
            } else {
                binding.connectBtn.setText("Connect");
                binding.connectionStatus.setText("");
            }
        });

        viewModel.getMessageLiveData().observe(getViewLifecycleOwner(), message -> showMessage("Message Received!", message));

        viewModel.getLoadingLiveData().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                binding.loading.setVisibility(View.VISIBLE);
            } else {
                binding.loading.setVisibility(View.GONE);
            }
        });

    }

    private void setupView() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.textInputContainer, (v, insets) -> {
            if (!insetsApplied) {
                Insets bars = insets.getInsets(
                        WindowInsetsCompat.Type.navigationBars()
                );
                v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), bars.bottom + v.getPaddingBottom());
                insetsApplied = true;
            }
            return WindowInsetsCompat.CONSUMED;
        });
        binding.usersListBtn.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_home_to_userList)
        );

        binding.connectBtn.setOnClickListener(v -> {
                    if (Boolean.TRUE.equals(viewModel.getConnectionStatusLiveData().getValue())) {
                        viewModel.disconnect();
                    } else viewModel.connect();
                }
        );

        binding.inputField.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                viewModel.sendMessage(Objects.requireNonNull(binding.inputField.getText()).toString());
                v.clearFocus();
                binding.inputField.setText("");
                return true;
            }
            return false;
        });

        binding.sendBtn.setOnClickListener(v -> {
            viewModel.sendMessage(Objects.requireNonNull(binding.inputField.getText()).toString());
            binding.inputField.clearFocus();
            binding.inputField.setText("");
        });
    }

    private void showMessage(String title, String message) {
        new MaterialAlertDialogBuilder(this.requireContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss()).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        insetsApplied = false;
        binding = null;
    }
}
