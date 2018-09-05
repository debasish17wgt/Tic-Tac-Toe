package com.wgt.tictactoe.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wgt.tictactoe.R;
import com.wgt.tictactoe.databinding.OnlineUsersViewBinding;
import com.wgt.tictactoe.model.User;

import java.util.List;

public class OnlineUsersAdapter extends RecyclerView.Adapter<OnlineUsersAdapter.ViewHolder> {

    private OnlineUsersClickListener listener;
    private List<User> users;
    private LayoutInflater inflater;

    public OnlineUsersAdapter(OnlineUsersClickListener listener, List<User> users) {
        this.listener = listener;
        this.users = users;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final OnlineUsersViewBinding binding;

        public ViewHolder(OnlineUsersViewBinding viewBinding) {
            super(viewBinding.getRoot());
            binding = viewBinding;
        }
    }

    public interface OnlineUsersClickListener {
        void onOnlineUserClick(User user);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        OnlineUsersViewBinding binding = DataBindingUtil.inflate(inflater, R.layout.online_users_view, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.setUser(users.get(position));
        holder.binding.layoutOnlineUser.setOnClickListener(v -> {
            if (listener!=null) {
                listener.onOnlineUserClick(users.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


}
