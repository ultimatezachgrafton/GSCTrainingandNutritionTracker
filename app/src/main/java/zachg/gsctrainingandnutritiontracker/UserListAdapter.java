package zachg.gsctrainingandnutritiontracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


// UserListAdapter adapts the RecyclerView list items of Users for viewing

public class UserListAdapter extends FirestoreRecyclerAdapter<User, UserListAdapter.UserViewHolder> {

    public UserListAdapter(@NonNull FirestoreRecyclerOptions<User> users) {
        super(users);
    }

    // UserViewHolder is the class that defines the views that hold the User data
    class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmail;
        TextView tvClientName;

        public UserViewHolder(View itemView) {
            super(itemView);
            tvClientName = itemView.findViewById(R.id.etClientName);
            tvEmail = itemView.findViewById(R.id.etEmail);
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,
                parent, false);
        return new UserViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User user) {
        holder.tvClientName.setText(user.getClientName());
        holder.tvEmail.setText(user.getEmail());
    }

}
