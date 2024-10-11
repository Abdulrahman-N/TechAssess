package com.abdulrahman.technicalassessment.userList.presentation;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.abdulrahman.technicalassessment.R;
import com.abdulrahman.technicalassessment.databinding.FragmentUserListBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserListFragment extends Fragment {

    private FragmentUserListBinding binding;
    private UserListViewModel viewModel;
    private UserListAdapter adapter;

    public UserListFragment() {
        super(R.layout.fragment_user_list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentUserListBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(UserListViewModel.class);
        setupView();
        setupObservers();
    }

    private void setupView() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.recyclerView, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(0, 0, 0, insets.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
        adapter = new UserListAdapter();
        binding.recyclerView.setAdapter(adapter);
        binding.toolbar.setNavigationOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());
    }

    private void setupObservers() {
        viewModel.userList.observe(getViewLifecycleOwner(), users -> adapter.submitList(users));

        viewModel.error.observe(getViewLifecycleOwner(), this::showMessage);

        viewModel.loading.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                binding.loading.setVisibility(View.VISIBLE);
            } else {
                binding.loading.setVisibility(View.GONE);
            }
        });
    }

    private void showMessage(String message) {
        new MaterialAlertDialogBuilder(this.requireContext())
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss()).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
