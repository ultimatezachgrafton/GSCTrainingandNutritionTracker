package zachg.gsctrainingandnutritiontracker.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import zachg.gsctrainingandnutritiontracker.databinding.RvuserItemBinding;
import zachg.gsctrainingandnutritiontracker.models.User;

// UserListAdapter adapts the RecyclerView list items of Users for viewing

public class UserListAdapter extends FirestoreRecyclerAdapter<User, UserListAdapter.UserViewHolder> {
    User currentUser = new User();

    // Listens for a Firestore query
    public UserListAdapter(@NonNull FirestoreRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position, User user) {
        holder.bind(user);
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