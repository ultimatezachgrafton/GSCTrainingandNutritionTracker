package zachg.gsctrainingandnutritiontracker.adapters;

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
import zachg.gsctrainingandnutritiontracker.models.Report;

public class ReportListAdapter extends FirestoreRecyclerAdapter<Report, ReportListAdapter.ReportViewHolder> {
    private OnItemClickListener listener;
    private Report reportList = new Report();
    public String TAG = "ReportListAdapter";

    // Listens for a Firestore query
    public ReportListAdapter(@NonNull FirestoreRecyclerOptions<Report> options) {
        super(options);
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvreport_item, parent, false);

        return new ReportViewHolder(v);
    }

    // UserViewHolder is the class that defines the views that hold the User data
    class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView tvWorkoutTitle;
        TextView tvWorkoutDay;
        TextView tvClientName;
        TextView tvDateString;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWorkoutTitle = itemView.findViewById(R.id.tvWorkoutTitle);
            tvClientName = itemView.findViewById(R.id.tvClientName);
            tvDateString = itemView.findViewById(R.id.tvDateString);

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
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position, Report report) {
        holder.tvWorkoutTitle.setText(report.getWorkoutTitle());
        //holder.tvWorkoutDay.setText(report.getWorkoutDay());
        holder.tvDateString.setText(report.getDateString());
        holder.tvClientName.setText(report.getClientName());
    }

}

