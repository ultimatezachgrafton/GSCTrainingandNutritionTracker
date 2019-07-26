package zachg.gsctrainingandnutritiontracker.UI.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import zachg.gsctrainingandnutritiontracker.Models.Report;
import zachg.gsctrainingandnutritiontracker.R;

public class ReportListAdapter extends FirestoreRecyclerAdapter<Report, ReportListAdapter.ReportViewHolder> {

    public ReportListAdapter(@NonNull FirestoreRecyclerOptions<Report> options) {
        super(options);
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvworkout_item,
                parent, false);
        return new ReportViewHolder(v);
    }

    // ReportViewHolder is the class that defines the views that hold the User data
    class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView tvDateSent;
        TextView tvClientName;
        TextView tvExerciseName;
        TextView tvReps;
        ImageView check;

        public ReportViewHolder(View itemView) {
            super(itemView);
            tvDateSent = itemView.findViewById(R.id.tvDate);
            tvClientName = itemView.findViewById(R.id.tvClientName);
//            tvExerciseName.setText(currentSelectedWorkout.getExerciseName());
//            tvReps.setText(currentSelectedWorkout.getReps());

//            if (currentSelectedReport.getIsNew()) {
//                check = itemView.findViewById(R.id.check);
//            }
        }
    }

    @Override
    protected void onBindViewHolder(@NonNull ReportViewHolder holder, int position, @NonNull Report report) {
    }
}