package zachg.gsctrainingandnutritiontracker.inbox;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import zachg.gsctrainingandnutritiontracker.R;

public class MsgListAdapter extends FirestoreRecyclerAdapter<Msg, MsgListAdapter.MsgViewHolder> {
    private OnItemClickListener listener;

    public MsgListAdapter(@NonNull FirestoreRecyclerOptions<Msg> msgs) {
        super(msgs);
    }

    @NonNull
    @Override
    public MsgListAdapter.MsgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvinbox_item,
                parent, false);
        //v.setOnClickListener(mOnClickListener);
        return new MsgListAdapter.MsgViewHolder(v);
    }

    // MsgViewHolder is the class that defines the views that hold the User data
    class MsgViewHolder extends RecyclerView.ViewHolder {
        TextView tvMsgTitle;
        TextView tvMsgBody;
        TextView tvDateSent;
        TextView tvClientName;

        public MsgViewHolder(View itemView) {
            super(itemView);
            tvMsgTitle = itemView.findViewById(R.id.tvMsgTitle);
            tvMsgBody = itemView.findViewById(R.id.tvMsgBody);

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
    }

    @Override
    protected void onBindViewHolder(@NonNull MsgViewHolder holder, int position, @NonNull Msg msg) {
        holder.tvMsgTitle.setText(msg.getMsgTitle());
        holder.tvMsgBody.setText(msg.getMsgBody());
        //holder.tvDateSent.setText(msg.getMsgDate());
        //holder.tvClientName.setText(msg.getClientName());
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot doc, int position);
    }
    public void setOnItemClickListener(MsgListAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}