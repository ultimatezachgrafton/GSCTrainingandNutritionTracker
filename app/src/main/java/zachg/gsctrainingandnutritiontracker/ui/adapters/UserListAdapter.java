package zachg.gsctrainingandnutritiontracker.ui.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.databinding.RvuserItemBinding;
import zachg.gsctrainingandnutritiontracker.models.User;

// UserListAdapter adapts the RecyclerView list items of Users for viewing

public class UserListAdapter extends FirestoreRecyclerAdapter<User, UserListAdapter.UserViewHolder> {
    private ArrayList<User> users;
    User currentUser = new User();

    // Listens for a Firestore query
    public UserListAdapter(@NonNull FirestoreRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position, User user) {
        User currentUser = users.get(position);
        holder.binding.setUser(currentUser);
        Log.d("plum", "onbind");
    }

    @Override
    public int getItemCount() {
        if (users != null) {
            return users.size(); }
        else {
            return 0;
        }
    }

    public void setUserList(ArrayList<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RvuserItemBinding binding = RvuserItemBinding.inflate(layoutInflater, parent, false);
        return new UserViewHolder(binding);
    }

    // UserViewHolder is the class that defines the views that hold the User data
    class UserViewHolder extends RecyclerView.ViewHolder {
        private RvuserItemBinding binding;

        public UserViewHolder(@NonNull RvuserItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(User user) {
            binding.setUser(user);
            binding.executePendingBindings();
        }
    }
}