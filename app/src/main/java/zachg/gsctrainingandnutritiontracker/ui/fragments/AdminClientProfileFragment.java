package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentAdminClientProfileBinding;
import zachg.gsctrainingandnutritiontracker.models.Exercise;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.viewmodels.AdminClientProfileViewModel;
import zachg.gsctrainingandnutritiontracker.viewmodels.RegisterViewModel;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AdminClientProfileFragment extends Fragment {

    // The admin enters workout values and sends them to FirestoreRepository

    FragmentAdminClientProfileBinding binding;

    private AdminClientProfileViewModel adminClientProfileViewModel = new AdminClientProfileViewModel();
    private final String ARG_USER_ID = "user_id";

    private User currentUser = new User();
    private Exercise exercise = new Exercise();
    private Exercise exercise2 = new Exercise();
    private Exercise exercise3 = new Exercise();
    private Exercise exercise4 = new Exercise();
    private Exercise exercise5 = new Exercise();
    private Exercise exercise6 = new Exercise();
    private Exercise exercise7 = new Exercise();
    private int totalEditTexts;

    public AdminClientProfileFragment() {}

    public AdminClientProfileFragment(User user) {
        this.currentUser = user;
        //adminClientProfileViewModel = new AdminClientProfileViewModel(user); // remove
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment_report_list
        binding = FragmentAdminClientProfileBinding.inflate(inflater, container, false);
        View v = binding.getRoot();
        binding.setFragment(this);
        binding.setUser(currentUser);
        binding.setExercise(exercise);
        binding.setExercise(exercise2);
        binding.setExercise(exercise3);
        binding.setExercise(exercise4);
        binding.setExercise(exercise5);
        binding.setExercise(exercise6);
        binding.setExercise(exercise7);
        Workout workout = new Workout(currentUser);
        binding.setWorkout(workout);

        binding.setViewmodel(adminClientProfileViewModel);
        adminClientProfileViewModel = ViewModelProviders.of(this).get(AdminClientProfileViewModel.class);
        adminClientProfileViewModel.init();

        adminClientProfileViewModel.newExercise.observe(this, new Observer<Exercise>() {
            @Override
            public void onChanged(Exercise exercise) {
                    Log.d(TAG, "changed ex");
            }
        });

        adminClientProfileViewModel.newExercise2.observe(this, new Observer<Exercise>() {
            @Override
            public void onChanged(Exercise exercise) {
                Log.d(TAG, "changed ex");
            }
        });

        adminClientProfileViewModel.newExercise3.observe(this, new Observer<Exercise>() {
            @Override
            public void onChanged(Exercise exercise) {
                Log.d(TAG, "changed ex");
            }
        });

        adminClientProfileViewModel.newExercise4.observe(this, new Observer<Exercise>() {
            @Override
            public void onChanged(Exercise exercise) {
                Log.d(TAG, "changed ex");
            }
        });

        adminClientProfileViewModel.newExercise5.observe(this, new Observer<Exercise>() {
            @Override
            public void onChanged(Exercise exercise) {
                Log.d(TAG, "changed ex");
            }
        });

        adminClientProfileViewModel.newExercise6.observe(this, new Observer<Exercise>() {
            @Override
            public void onChanged(Exercise exercise) {
                Log.d(TAG, "changed ex");
            }
        });

        adminClientProfileViewModel.newExercise7.observe(this, new Observer<Exercise>() {
            @Override
            public void onChanged(Exercise exercise) {
                Log.d(TAG, "changed ex");
            }
        });

        adminClientProfileViewModel.newWorkout.observe(this, new Observer<Workout>() {
            @Override
            public void onChanged(Workout workout) {
                Log.d(TAG, "changed ex");
            }
        });

        return v;
    }

    public void createExerciseArray() {

    }

    public void addExerciseValues(Exercise exercise, Exercise exercise2, Exercise exercise3,
                                  Exercise exercise4, Exercise exercise5, Exercise exercise6,
                                  Exercise exercise7, String workoutDay, String workoutTitle) {
        adminClientProfileViewModel.writeToWorkouts(currentUser, exercise, exercise2, exercise3, exercise4, exercise5,
                exercise6, exercise7, workoutDay, workoutTitle);
    }

    public void toDatePicker() {
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new ViewReportFragment()).addToBackStack(null).commit();
        //SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
        //                new AdminCalendarFragment(currentUser)).addToBackStack(null).commit();
    }
}
