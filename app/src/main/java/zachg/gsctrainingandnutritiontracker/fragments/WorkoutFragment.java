package zachg.gsctrainingandnutritiontracker.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.adapters.ExerciseListAdapter;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentWorkoutBinding;
import zachg.gsctrainingandnutritiontracker.models.Exercise;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.viewmodels.AdminClientProfileViewModel;
import zachg.gsctrainingandnutritiontracker.viewmodels.WorkoutViewModel;

public class WorkoutFragment extends Fragment {

    private FragmentWorkoutBinding binding;
    private ExerciseListAdapter exerciseListAdapter;
    private WorkoutViewModel workoutViewModel = new WorkoutViewModel();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private User user = new User();
    private User client = new User();
    private Workout workout = new Workout();
    private Button bAddExercise;
    public String TAG = "WorkoutFragment";

    public WorkoutFragment() {}

    public WorkoutFragment(Workout workout) {
    }

    public WorkoutFragment(User user, User client, Workout workout) {
        this.user = user;
        this.client = client;
        this.workout = workout;
        workout.setIsNew(false);
;    }

    public WorkoutFragment(User user, User client, String workoutTitle) {
        this.user = user;
        this.client = client;
        this.workout.setWorkoutTitle(workoutTitle);
        workout.setIsNew(true);
    }

    public WorkoutFragment(User user, User client) {
        this.user = user;
        this.client = client;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWorkoutBinding.inflate(inflater, container, false);
        View v = binding.getRoot();
        binding.setFragment(this);
        binding.setWorkout(workout);
        binding.setClient(client);
        binding.setBAddExercise(bAddExercise);
        binding.setModel(workoutViewModel);

        workoutViewModel = ViewModelProviders.of(getActivity()).get(WorkoutViewModel.class);
        workoutViewModel.init(client, workout);

        Workout workout = new Workout(client);
        binding.setWorkout(workout);

        workoutViewModel.workoutTitleLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String str) {
                workout.setWorkoutTitle(str);
            }
        });

        workoutViewModel.getExerciseLiveData().observe(this, new Observer<FirestoreRecyclerOptions<Exercise>>() {
            @Override
            public void onChanged(FirestoreRecyclerOptions<Exercise> e) {
                initRecyclerView(e);
                exerciseListAdapter.startListening();
            }
        });

        workoutViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (!aBoolean) {
                    binding.rvExercise.smoothScrollToPosition(workoutViewModel.getExerciseLiveData().getValue().getSnapshots().size() - 1);
                }
            }
        });

        workoutViewModel.onError.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                // TODO: getFocus
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

        workoutViewModel.workoutDeleted.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bool) {
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new WorkoutListFragment(client)).addToBackStack(null).commit();
            }
        });

        return v;
    }

    private void initRecyclerView(FirestoreRecyclerOptions<Exercise> exercises) {
        exerciseListAdapter = new ExerciseListAdapter(exercises);
        binding.rvExercise.setAdapter(exerciseListAdapter);
        binding.rvExercise.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void addOne() {}

    public void addThree() {}

    public void addFive() {}

    @Override
    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
        exerciseListAdapter.stopListening();
    }
}
