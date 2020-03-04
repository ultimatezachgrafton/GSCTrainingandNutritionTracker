package zachg.gsctrainingandnutritiontracker.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.adapters.WorkoutListAdapter;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentWorkoutListBinding;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.viewmodels.WorkoutListViewModel;

public class WorkoutListFragment extends Fragment {

    private FragmentWorkoutListBinding binding;
    private WorkoutListViewModel workoutListViewModel;
    private WorkoutListAdapter workoutListAdapter;

    private String TAG = "WorkoutListFragment";

    private User client = new User();
    private Workout workout = new Workout();

    public WorkoutListFragment(User user, User client) {
        this.client = client;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWorkoutListBinding.inflate(inflater, container, false);
        final View v = binding.getRoot();
        binding.setWorkout(workout);

        // Gets ViewModel instance to observe  LiveData
        binding.setModel(workoutListViewModel);
        workoutListViewModel = ViewModelProviders.of(getActivity()).get(WorkoutListViewModel.class);
        workoutListViewModel.init(client);
        workoutListViewModel.getWorkouts().observe(this, new Observer<FirestoreRecyclerOptions<Workout>>() {
            @Override
            public void onChanged(@Nullable FirestoreRecyclerOptions<Workout> workouts) {
                Log.d(TAG, "workouts changed");
                initRecyclerView(workouts);
                workoutListAdapter.startListening();
            }
        });

        workoutListViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (!aBoolean) {
                    binding.rvWorkout.smoothScrollToPosition(workoutListViewModel.getWorkouts().getValue().getSnapshots().size() - 1);
                }
            }

        });

        return v;
    }

    private void initRecyclerView(FirestoreRecyclerOptions<Workout> workouts) {
        workoutListAdapter = new WorkoutListAdapter(workouts);
        binding.rvWorkout.setAdapter(workoutListAdapter);

        Log.d(TAG, "initRecyclerView");

        binding.rvWorkout.setHasFixedSize(true);
        binding.rvWorkout.setLayoutManager(new LinearLayoutManager(getContext()));

        workoutListAdapter.setOnItemClickListener(new WorkoutListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                workout = workoutListViewModel.onItemClicked(documentSnapshot, position);
                // Goes to client's profile fragment_report_list
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new WorkoutFragment(client, workout)).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
        workoutListAdapter.stopListening();
    }
}
