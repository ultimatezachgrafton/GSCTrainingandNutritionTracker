package zachg.gsctrainingandnutritiontracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.adapters.ExerciseListAdapter;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentAdminUpdateWorkoutBinding;
import zachg.gsctrainingandnutritiontracker.models.Exercise;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.viewmodels.AdminUpdateWorkoutViewModel;

public class AdminUpdateWorkoutFragment extends Fragment {

    private FragmentAdminUpdateWorkoutBinding binding;
    private ExerciseListAdapter exerciseListAdapter;
    private AdminUpdateWorkoutViewModel adminUpdateWorkoutViewModel = new AdminUpdateWorkoutViewModel();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private User user = new User();
    private User client = new User();
    private Report report = new Report();
    private String workoutTitle;
    private Workout workout = new Workout();
    private Button bAddExercise;
    public String TAG = "WorkoutFragment";

    public AdminUpdateWorkoutFragment(User user, User client, Workout workout, Report report) {
        this.user = user;
        this.client = client;
        this.workout = workout;
        report.setIsNew(false);
        workout.setIsNew(false);
        this.report = report;
;    }

    public AdminUpdateWorkoutFragment(User user, User client, String workoutTitle) {
        this.user = user;
        this.client = client;
        this.workoutTitle = workoutTitle;
        workout.setIsNew(true);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminUpdateWorkoutBinding.inflate(inflater, container, false);
        View v = binding.getRoot();
        binding.setFragment(this);
        binding.setClient(client);
        binding.setBAddExercise(bAddExercise);
        binding.setModel(adminUpdateWorkoutViewModel);


        Workout workout = new Workout(client, workoutTitle);
        binding.setWorkout(workout);

        adminUpdateWorkoutViewModel = ViewModelProviders.of(getActivity()).get(AdminUpdateWorkoutViewModel.class);
        adminUpdateWorkoutViewModel.init(client, workout);

        adminUpdateWorkoutViewModel.getWorkoutTitleLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String str) {
                workout.setWorkoutTitle(str);
            }
        });

        adminUpdateWorkoutViewModel.getExerciseLiveData().observe(this, new Observer<FirestoreRecyclerOptions<Exercise>>() {
            @Override
            public void onChanged(FirestoreRecyclerOptions<Exercise> e) {
                initRecyclerView(e);
                exerciseListAdapter.startListening();
            }
        });

        adminUpdateWorkoutViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (!aBoolean) {
                    binding.rvExercise.smoothScrollToPosition(adminUpdateWorkoutViewModel.getExerciseLiveData().getValue().getSnapshots().size() - 1);
                }
            }
        });

        adminUpdateWorkoutViewModel.getOnError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

        adminUpdateWorkoutViewModel.getOnSuccess().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
            }
        });

        adminUpdateWorkoutViewModel.getWorkoutDeleted().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bool) {
                removeObservers();
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new AdminClientProfileFragment(user, client)).addToBackStack(null).commit();
            }
        });

        return v;
    }

    private void initRecyclerView(FirestoreRecyclerOptions<Exercise> exercises) {
        exerciseListAdapter = new ExerciseListAdapter(exercises);
        binding.rvExercise.setAdapter(exerciseListAdapter);
        binding.rvExercise.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void addOne() {
        adminUpdateWorkoutViewModel.addOne();
    }

    public void addThree() {
        adminUpdateWorkoutViewModel.addThree();
    }

    public void addFive() {
        adminUpdateWorkoutViewModel.addFive();
    }

    public void validateWorkout(User client, Workout workout) {
        adminUpdateWorkoutViewModel.nullWorkoutTitleCheck(client, workout);
    }

    public void deleteWorkout(User client, Workout workout) {
        adminUpdateWorkoutViewModel.deleteWorkout(client, workout);
    }

    public void onStop() {
        super.onStop();
        exerciseListAdapter.stopListening();
    }

    public void removeObservers() {
        adminUpdateWorkoutViewModel.getWorkoutDeleted().removeObservers(this);
        adminUpdateWorkoutViewModel.getExerciseLiveData().removeObservers(this);
        adminUpdateWorkoutViewModel.getIsUpdating().removeObservers(this);
        adminUpdateWorkoutViewModel.getWorkoutTitleLiveData().removeObservers(this);
        adminUpdateWorkoutViewModel.getOnError().removeObservers(this);
        adminUpdateWorkoutViewModel.getOnSuccess().removeObservers(this);
    }
}