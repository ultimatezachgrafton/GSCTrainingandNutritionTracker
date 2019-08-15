package zachg.gsctrainingandnutritiontracker.UI.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.Models.Workout;
import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.Repositories.FirestoreRepository;
import zachg.gsctrainingandnutritiontracker.ViewModels.ReportViewModel;

import static zachg.gsctrainingandnutritiontracker.Repositories.FirestoreRepository.sWorkouts;
import static zachg.gsctrainingandnutritiontracker.UI.Fragments.AdminListFragment.currentSelectedUser;
import static zachg.gsctrainingandnutritiontracker.ViewModels.ReportViewModel.currentSelectedWorkout;

// Displays Report data for current date and user, and a RecyclerView for user's Workouts
public class WorkoutListAdapter extends FirestoreRecyclerAdapter<Workout, WorkoutListAdapter.WorkoutViewHolder> {

    public WorkoutListAdapter(@NonNull FirestoreRecyclerOptions<Workout> options) {
        super(options);
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvworkout_item,
                parent, false);
        return new WorkoutViewHolder(v);
    }

    // ReportViewHolder defines the views that hold the Workout data
    class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private TextView tvExerciseName;
        private TextView tvReps;
        private EditText etWeightUsed;

        public WorkoutViewHolder(View itemView) {
            super(itemView);
            tvExerciseName = itemView.findViewById(R.id.tvExerciseName);
            tvReps = itemView.findViewById(R.id.tvReps);
            etWeightUsed = itemView.findViewById(R.id.etWeightUsed);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position, Workout workout) {
        // Sets the name of the workout and number of reps from admin's Firestore database

        // works if exerciseName is hardcoded here in setText()
        // but if it involves mWorkouts it fails
        // mWorkouts needs to be filled out
        holder.tvExerciseName.setText(workout.getExerciseName());
        Log.d("mReports", workout.getExerciseName());
//        holder.tvReps.setText(workout.getReps());
    }
}