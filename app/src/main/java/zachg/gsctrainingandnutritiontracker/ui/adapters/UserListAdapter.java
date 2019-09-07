package zachg.gsctrainingandnutritiontracker.ui.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.databinding.RvuserItemBinding;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.models.Workout;

// UserListAdapter adapts the RecyclerView list items of Users for viewing

public class UserListAdapter extends FirestoreRecyclerAdapter<User, UserListAdapter.UserViewHolder> {
    private OnItemClickListener listener;
    private User mCurrentUser = new User();

    public UserListAdapter(@NonNull FirestoreRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User user) {
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
        private final RvuserItemBinding binding;

        public UserViewHolder(RvuserItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
        public void bind(User user) {
            binding.setUser(user);
            binding.executePendingBindings();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot doc, int position);
    }

    public User getUserAtPosition(User user) {
        mCurrentUser = user;
        return mCurrentUser;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}