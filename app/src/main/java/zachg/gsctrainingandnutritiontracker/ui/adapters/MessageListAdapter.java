package zachg.gsctrainingandnutritiontracker.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import zachg.gsctrainingandnutritiontracker.databinding.RvinboxItemBinding;
import zachg.gsctrainingandnutritiontracker.models.Message;

public class MessageListAdapter extends FirestoreRecyclerAdapter<Message, MessageListAdapter.MessageViewHolder> {
    private OnItemClickListener listener;
    private Message currentMessage = new Message();

    public MessageListAdapter(@NonNull FirestoreRecyclerOptions<Message> msgs) {
        super(msgs);
    }

    @Override
    protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull Message msg) {
        holder.bind(msg);
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RvinboxItemBinding binding = RvinboxItemBinding.inflate(layoutInflater, parent, false);

        return new MessageViewHolder(binding);
    }

    // MsgViewHolder is the class that defines the views that hold the User data
    class MessageViewHolder extends RecyclerView.ViewHolder {
        private final RvinboxItemBinding binding;

        public MessageViewHolder(RvinboxItemBinding binding) {
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
        public void bind(Message message) {
            binding.setMessage(message);
            binding.executePendingBindings();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot doc, int position);
        // TODO: if msg is unread,
        // switch boolean from unread to read
        // if msg is read, show check
        // setIsRead(true);
    }

    public Message getMessageAtPosition(Message message) {
        currentMessage = message;
        return currentMessage;
    }

    public void setOnItemClickListener(MessageListAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}