package zachg.gsctrainingandnutritiontracker.ui.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.core.Repo;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.databinding.RvworkoutItemBinding;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.ui.fragments.ReportFragment;
import zachg.gsctrainingandnutritiontracker.viewmodels.ReportViewModel;

// Displays Report data for current date and user, and a RecyclerView for user's Workouts
public class WorkoutListAdapter extends FirestoreRecyclerAdapter<Workout, WorkoutListAdapter.WorkoutViewHolder> {

    private ArrayList<String> name = new ArrayList<>();
    private int i = 0;
    private ReportViewModel reportViewModel = new ReportViewModel();

    public WorkoutListAdapter(FirestoreRecyclerOptions<Workout> options) { super(options); }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position, Workout workout) {
        holder.bind(workout);
//        name.set(i, workout.getExerciseName());
//        i++;
//        if(i == options.getSize()) {
//            reportViewModel.getNames();
//        }
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RvworkoutItemBinding binding = RvworkoutItemBinding.inflate(layoutInflater, parent, false);

        return new WorkoutViewHolder(binding);
    }

    // ReportViewHolder defines the views that hold the Workout data
    class WorkoutViewHolder extends RecyclerView.ViewHolder {
       private final RvworkoutItemBinding binding;

        public WorkoutViewHolder(RvworkoutItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Workout workout) {
            binding.setWorkout(workout);
            binding.executePendingBindings();
        }
    }
}