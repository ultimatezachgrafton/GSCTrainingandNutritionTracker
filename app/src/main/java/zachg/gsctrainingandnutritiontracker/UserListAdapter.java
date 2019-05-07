package zachg.gsctrainingandnutritiontracker;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

// UserListAdapter adapts the RecyclerView list items of Users for viewing

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {

    class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //private final TextView userItemView;
        private User mUser;
        private TextView mTitleTextView;

        public UserViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.recyclerview_item, parent, false));
            itemView.setOnClickListener(this);
            //userItemView = itemView.findViewById(R.id.userListItem);
        }

        public void bind(User user) {
            mUser = user;
            mTitleTextView.setText(mUser.getClientName());
        }

        @Override
        public void onClick(View view) {
            Intent intent = PagerActivity.newIntent(getActivity(), mUser.getId());
            startActivity(intent);
        }
    }

    private List<User> mUsers; // Cached copy of users
    private LayoutInflater mInflater;
    private Context mContext;

    public UserListAdapter(Context context, List<User> users) {
        mUsers = users;
        this.mContext = context;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        return new UserViewHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        if (mUsers != null) {
            User current = mUsers.get(position);
            holder.bind(current);
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
