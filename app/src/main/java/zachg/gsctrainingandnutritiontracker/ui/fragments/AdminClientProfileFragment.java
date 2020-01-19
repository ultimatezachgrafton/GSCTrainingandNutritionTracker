package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentAdminClientProfileBinding;
import zachg.gsctrainingandnutritiontracker.models.Exercise;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.viewmodels.AdminClientProfileViewModel;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AdminClientProfileFragment extends Fragment {

    // Enter workout values and send them to FirestoreRepository

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
        adminClientProfileViewModel = new AdminClientProfileViewModel(user);
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
        final Workout workout = new Workout(currentUser);
        workout.setWorkoutTitle("awooga");
        binding.setWorkout(workout);
        adminClientProfileViewModel.init();
        binding.setViewmodel(adminClientProfileViewModel);

        totalEditTexts = 0;

        adminClientProfileViewModel.newExercise.observe(this, new Observer<Exercise>() {
            @Override
            public void onChanged(Exercise exercise) {
                    Log.d(TAG, "changed ex");
            }
        });

        return v;
    }

    public void createExerciseArray() {

    }

    public void addExercise() {
//        totalEditTexts++;
//        if (totalEditTexts > 15)
//            return;
//        EditText newEditText = new EditText(context); // Pass it an Activity or Context
//        newEditText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
//        getRoot.addView(newEditText);
    }

    public void toDatePicker() {
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new ViewReportFragment()).addToBackStack(null).commit();
        //SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
        //                new AdminCalendarFragment(currentUser)).addToBackStack(null).commit();
    }
}
