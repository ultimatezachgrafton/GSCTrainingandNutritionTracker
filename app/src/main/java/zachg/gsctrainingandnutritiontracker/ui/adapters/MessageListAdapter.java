package zachg.gsctrainingandnutritiontracker.ui.adapters;

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
import zachg.gsctrainingandnutritiontracker.models.Message;

public class MessageListAdapter extends FirestoreRecyclerAdapter<Message, MessageListAdapter.MessageViewHolder> {
    private OnItemClickListener listener;
    private Message currentMessage = new Message();
    public String TAG = "MessageListAdapter";

    // Listens for Firestore query
    public MessageListAdapter(@NonNull FirestoreRecyclerOptions<Message> options) {
        super(options);
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvinbox_item, parent, false);

        return new MessageViewHolder(v);
    }

    // MsgViewHolder is the class that defines the views that hold the User data
    class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvSenderName;
        TextView tvMessageTitle;

        public MessageViewHolder(View itemView) {
            super(itemView);
            tvSenderName = itemView.findViewById(R.id.tvSenderName);
            tvMessageTitle = itemView.findViewById(R.id.tvMessageTitle);

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

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
        // TODO: if msg is unread,
        // switch boolean from unread to read
        // if msg is read, show check
        // setIsRead(true);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, Message message) {
        holder.tvSenderName.setText(message.getClientName());
        holder.tvMessageTitle.setText(message.getBody());
    }
}