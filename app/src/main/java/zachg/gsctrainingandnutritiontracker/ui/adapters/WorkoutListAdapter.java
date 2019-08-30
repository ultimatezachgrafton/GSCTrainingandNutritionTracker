package zachg.gsctrainingandnutritiontracker.ui.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.models.Workout;

// Displays Report data for current date and user, and a RecyclerView for user's Workouts
public class WorkoutListAdapter extends FirestoreRecyclerAdapter<Workout, WorkoutListAdapter.WorkoutViewHolder> {

    public WorkoutListAdapter(FirestoreRecyclerOptions<Workout> options) {
        super(options);
        Log.d("mReports", "adapter constructor");
    }

    @NonNull
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvworkout_item, parent, false);
        Log.d("mReports", "onCreateViewHolder");
//        RvworkoutItemBinding binding = RvworkoutItemBinding.inflate(layoutInflater, parent, false);

        return new WorkoutViewHolder(v);//binding);
    }

    // ReportViewHolder defines the views that hold the Workout data
    class WorkoutViewHolder extends RecyclerView.ViewHolder {
       // private final RvworkoutItemBinding binding;
        private TextView exName;
//        private TextView tvReps;
//        private EditText etWeightUsed;

        public WorkoutViewHolder(View itemView) {//RvworkoutItemBinding binding) {
            super(itemView);
            exName = itemView.findViewById(R.id.tvExerciseName);
//            super(binding.getRoot());
            //this.binding = binding;
        }

//        public void bind(Workout workout) {
//            binding.setWorkout(workout);
//            binding.executePendingBindings();
//        }
    }

//    public int getItemCount() {
//        //Log.d("mReports", String.format("getItemCount: %d", workouts.size()));
//        return 1;//workouts.size();
//    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position, Workout workout) {
        holder.exName.setText(workout.getExName());
        //holder.bind(workout);
        Log.d("mReports", "onBindViewHolder: workout name:" + workout.getExName());
    }
}