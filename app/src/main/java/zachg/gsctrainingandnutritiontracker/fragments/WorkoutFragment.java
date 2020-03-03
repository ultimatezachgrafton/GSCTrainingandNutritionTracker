package zachg.gsctrainingandnutritiontracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import zachg.gsctrainingandnutritiontracker.adapters.ExerciseListAdapter;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentReportBinding;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentWorkoutBinding;
import zachg.gsctrainingandnutritiontracker.models.Exercise;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.viewmodels.ReportViewModel;
import zachg.gsctrainingandnutritiontracker.viewmodels.WorkoutViewModel;

public class WorkoutFragment extends Fragment {
    // For Users to fill out their workout as they complete it

    private FragmentWorkoutBinding binding;

    private WorkoutViewModel workoutViewModel = new WorkoutViewModel();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private ExerciseListAdapter exerciseListAdapter;

    private User client = new User();
    private Workout workout = new Workout();

    private static final int REQUEST_CONTACT = 1;
    public String TAG = "WorkoutFragment";

    public WorkoutFragment() {}

    public WorkoutFragment(Workout workout) {}

    public WorkoutFragment(User user, Workout workout) {
        this.client = user;
        this.workout = workout;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWorkoutBinding.inflate(inflater, container, false);
        final View v = binding.getRoot();
        binding.setWorkout(workout);
        binding.setUser(client);

        binding.setModel(workoutViewModel);
        workoutViewModel = ViewModelProviders.of(getActivity()).get(WorkoutViewModel.class);
        workoutViewModel.init(client, workout);

        workoutViewModel.getExerciseLiveData().observe(this, new Observer<FirestoreRecyclerOptions<Exercise>>() {
            @Override
            public void onChanged(FirestoreRecyclerOptions<Exercise> exercises) {
                initRecyclerView(exercises);
                exerciseListAdapter.startListening();
            }
        });

        workoutViewModel.getExercises().observe(this, new Observer<ArrayList<Exercise>>() {
            @Override
            public void onChanged(ArrayList<Exercise> exercises) {
                initRecyclerView(exercises);
                exerciseListAdapter.startListening();
            }
        });

        workoutViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (!aBoolean) {
                    binding.rvExercise.smoothScrollToPosition(workoutViewModel.getExercises().getValue().getSnapshots().size() - 1);
                }
            }

        });

        return v;

        // TODO: allows editing and deletion of workouts, exercises
    }

    private void initRecyclerView(ArrayList<Exercise> exercises) {
        exerciseListAdapter = new ExerciseListAdapter(exercises);
        binding.rvExercise.setAdapter(exerciseListAdapter);
        binding.rvExercise.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
        exerciseListAdapter.stopListening();
    }
}
