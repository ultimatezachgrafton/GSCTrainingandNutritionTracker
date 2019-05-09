package zachg.gsctrainingandnutritiontracker;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

// UserListAdapter adapts the RecyclerView list items of Users for viewing

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {

    class UserViewHolder extends RecyclerView.ViewHolder {
        //private final TextView userItemView;
        private TextView mTitleTextView;

        public UserViewHolder(View view) {
            super(view);
            mTitleTextView = (TextView)view.findViewById(R.id.userListItem);
        }

        public void bind(User user) {
            mTitleTextView.setText(user.getClientName());
        }
    }

    private List<User> mUsers; // Cached copy of users
    private LayoutInflater mInflater;
    private Context mContext;
    private View.OnClickListener mOnClickListener;

    public UserListAdapter(Context context, List<User> users, View.OnClickListener clickListener) {
        mUsers = users;
        this.mContext = context;
        mOnClickListener = clickListener;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new UserViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        if (mUsers != null) {
            User current = mUsers.get(position);
            holder.bind(current);
            holder.itemView.setOnClickListener(mOnClickListener);
            ((TextView)holder.itemView.findViewById(R.id.userListItem)).setText(current.getClientName());
        }
    }

    void setUsers(List<User> mUsers){
        this.mUsers = mUsers;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mUsers has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mUsers != null)
            return mUsers.size();
        else return 0;
    }
}
