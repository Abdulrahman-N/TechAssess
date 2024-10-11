package com.abdulrahman.technicalassessment.userList.presentation;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulrahman.technicalassessment.R;
import com.abdulrahman.technicalassessment.databinding.ItemUserBinding;
import com.abdulrahman.technicalassessment.userList.domain.User;

public class UserListAdapter extends ListAdapter<User, UserListAdapter.UserViewHolder> {
    public UserListAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<User> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserBinding binding = ItemUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = getItem(position);
        holder.bind(user);
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        private final ItemUserBinding binding;

        public UserViewHolder(ItemUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(User user) {
            binding.name.setText(user.getName());
            binding.email.setText(user.getEmail());
            binding.status.setText(user.getStatus());
            binding.gender.setText(user.getGender());
            if (user.getGender().equalsIgnoreCase("male")) {
                binding.gender.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.blue));
            } else
                binding.gender.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.pink));
            if (user.getStatus().equalsIgnoreCase("active")) {
                binding.status.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.green));
            } else
                binding.status.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.red));
        }
    }
}
