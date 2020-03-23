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
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.viewmodels.AdminUpdateWorkoutViewModel;

public class AdminUpdateWorkoutFragment extends Fragment {

    private FragmentAdminUpdateWorkoutBinding binding;
    private ExerciseListAdapter exerciseListAdapter;
    private AdminUpdateWorkoutViewModel mAdminUpdateWorkoutViewModel = new AdminUpdateWorkoutViewModel();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private User user = new User();
    private User client = new User();
    private Workout workout = new Workout();
    private Button bAddExercise;
    public String TAG = "WorkoutFragment";

    public AdminUpdateWorkoutFragment() {}

    public AdminUpdateWorkoutFragment(Workout workout) {
    }

    public AdminUpdateWorkoutFragment(User user, User client, Workout workout) {
        this.user = user;
        this.client = client;
        this.workout = workout;
        workout.setIsNew(false);
;    }

    public AdminUpdateWorkoutFragment(User user, User client, String workoutTitle) {
        this.user = user;
        this.client = client;
        this.workout.setWorkoutTitle(workoutTitle);
        workout.setIsNew(true);
    }

    public AdminUpdateWorkoutFragment(User user, User client) {
        this.user = user;
        this.client = client;
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
        binding.setWorkout(workout);
        binding.setClient(client);
        binding.setBAddExercise(bAddExercise);
        binding.setModel(mAdminUpdateWorkoutViewModel);

        mAdminUpdateWorkoutViewModel = ViewModelProviders.of(getActivity()).get(AdminUpdateWorkoutViewModel.class);
        mAdminUpdateWorkoutViewModel.init(client, workout);

        Workout workout = new Workout(client);
        binding.setWorkout(workout);

        mAdminUpdateWorkoutViewModel.workoutTitleLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String str) {
                workout.setWorkoutTitle(str);
            }
        });

        mAdminUpdateWorkoutViewModel.getExerciseLiveData().observe(this, new Observer<FirestoreRecyclerOptions<Exercise>>() {
            @Override
            public void onChanged(FirestoreRecyclerOptions<Exercise> e) {
                initRecyclerView(e);
                exerciseListAdapter.startListening();
            }
        });

        mAdminUpdateWorkoutViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (!aBoolean) {
                    binding.rvExercise.smoothScrollToPosition(mAdminUpdateWorkoutViewModel.getExerciseLiveData().getValue().getSnapshots().size() - 1);
                }
            }
        });

        mAdminUpdateWorkoutViewModel.onError.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                // TODO: getFocus
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

        mAdminUpdateWorkoutViewModel.workoutDeleted.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bool) {
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new AdminWorkoutListFragment(client)).addToBackStack(null).commit();
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
        // add one empty exercise
        // write to repo
    }

    public void addThree() {
        // create 3 empty exercises
    }

    public void addFive() {
        // create 5
        // write to repo
    }

    public void validateWorkout(User client, Workout workout) {
        mAdminUpdateWorkoutViewModel.nullWorkoutTitleCheck(client, workout);
    }

    public void deleteWorkout(User client, Workout workout) {
        // TODO: are you certain? y/n pop-up
        mAdminUpdateWorkoutViewModel.deleteWorkout(client, workout);
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
