package zachg.gsctrainingandnutritiontracker.ui.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import zachg.gsctrainingandnutritiontracker.databinding.RvuserItemBinding;
import zachg.gsctrainingandnutritiontracker.models.User;

// UserListAdapter adapts the RecyclerView list items of Users for viewing

public class UserListAdapter extends FirestoreRecyclerAdapter<User, UserListAdapter.UserViewHolder> {
    private User currentUser = new User();

    public UserListAdapter(@NonNull FirestoreRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User user) {
        holder.textViewTitle.setText(user.getClientName());
        Log.d("plum", "bind");
        //holder.bind(user);
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
        private final RvuserItemBinding binding;

        public UserViewHolder(RvuserItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
        public void bind(User user) {
            binding.setUser(user);
            binding.executePendingBindings();
        }
    }

    public User getUserAtPosition(User user) {
        currentUser = user;
        return currentUser;
    }

}