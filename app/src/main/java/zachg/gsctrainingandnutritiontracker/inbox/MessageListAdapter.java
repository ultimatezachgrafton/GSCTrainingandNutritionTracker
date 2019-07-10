package zachg.gsctrainingandnutritiontracker.inbox;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import zachg.gsctrainingandnutritiontracker.R;

public class MessageListAdapter extends FirestoreRecyclerAdapter<Message, MessageListAdapter.MessageViewHolder> {
    private OnItemClickListener listener;

    public MessageListAdapter(@NonNull FirestoreRecyclerOptions<Message> msgs) {
        super(msgs);
    }

    @NonNull
    @Override
    public MessageListAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvinbox_item,
                parent, false);
        return new MessageListAdapter.MessageViewHolder(v);
    }

    // MsgViewHolder is the class that defines the views that hold the User data
    class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvMsgTitle;
        TextView tvMsgBody;
        TextView tvDateSent;
        TextView tvClientName;
        ImageView check;

        public MessageViewHolder(View itemView) {
            super(itemView);
            tvMsgTitle = itemView.findViewById(R.id.tvMsgTitle);
            tvMsgBody = itemView.findViewById(R.id.tvMsgBody);
            // if msg is read
            check = itemView.findViewById(R.id.check);

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
    protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull Message msg) {
        holder.tvMsgTitle.setText(msg.getMsgTitle());
        holder.tvMsgBody.setText(msg.getMsgBody());
        //holder.tvDateSent.setText(msg.getMsgDate());
        //holder.tvClientName.setText(msg.getClientName());
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot doc, int position);
        // if msg is unread,
        // switch boolean from unread to read
        // if msg is read, show check
        // setIsRead(true);
    }

    public void setOnItemClickListener(MessageListAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}