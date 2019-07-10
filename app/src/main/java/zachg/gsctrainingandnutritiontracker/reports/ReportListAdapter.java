package zachg.gsctrainingandnutritiontracker.reports;

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

public class ReportListAdapter extends FirestoreRecyclerAdapter<Report, ReportListAdapter.ReportViewHolder> {
    private OnItemClickListener listener;

    public ReportListAdapter(@NonNull FirestoreRecyclerOptions<Report> reports) {
        super(reports);
    }

    @NonNull
    @Override
    public ReportListAdapter.ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvworkout_item,
                parent, false);
        return new ReportListAdapter.ReportViewHolder(v);
    }

    // MsgViewHolder is the class that defines the views that hold the User data
    class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView tvDateSent;
        TextView tvClientName;
        ImageView check;

        public ReportViewHolder(View itemView) {
            super(itemView);
            tvDateSent = itemView.findViewById(R.id.tvDate);
            tvClientName = itemView.findViewById(R.id.tvClientName);
            //if Report is new
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
    protected void onBindViewHolder(@NonNull ReportViewHolder holder, int position, @NonNull Report report) {
        holder.tvClientName.setText(report.getClientName());
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot doc, int position);

    }

    public void setOnItemClickListener(ReportListAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}