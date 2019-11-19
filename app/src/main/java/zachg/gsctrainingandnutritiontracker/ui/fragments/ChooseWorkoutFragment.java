package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentChooseWorkoutBinding;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.viewmodels.ChooseWorkoutViewModel;

public class ChooseWorkoutFragment extends DialogFragment {

    FragmentChooseWorkoutBinding binding;
    ChooseWorkoutViewModel chooseWorkoutViewModel;
    private ArrayList<Workout> workoutArray;
    private User user = new User();
    private Report report = new Report();
    private Workout workout = new Workout();
    private int workoutDay;
    private String TAG = "ChooseWorkoutFragment";

    ChooseWorkoutFragment(User user) {
        this.user = user;
    }

    // Define listener interface
    public interface ChooseWorkoutListener {
        void onFinishDialog(int workout);
    }

    // Send result back to parent fragment
    public void sendBackResult() {
        ChooseWorkoutListener listener = (ChooseWorkoutListener) getTargetFragment();
        listener.onFinishDialog(this.workoutDay);
        dismiss();
    }

    public static ChooseWorkoutFragment newInstance(User user) {
        ChooseWorkoutFragment frag = new ChooseWorkoutFragment(user);
        return frag;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentChooseWorkoutBinding.inflate(inflater, container, false);
        final View v = binding.getRoot();

        binding.setUser(user);

        chooseWorkoutViewModel = new ChooseWorkoutViewModel(user);
        binding.setViewModel(chooseWorkoutViewModel);

        chooseWorkoutViewModel.workoutLiveData.observe(this, new Observer<ArrayList<Workout>>() {
            @Override
            public void onChanged(ArrayList<Workout> w) {
                if (workout == null) {
                    Toast.makeText(getContext(), "That array does not exist.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "array time");
                    workoutArray = w;
                }
            }
        });

        chooseWorkoutViewModel.workoutSelected.observe(this, new Observer<Workout>() {
            @Override
            public void onChanged(Workout w) {
                goToReport(w);
            }
        });
        return v;
    }

    public void onSelectClicked() {
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new ReportFragment(report, user)).addToBackStack(null).commit();
    }

    public void goToReport(Workout workout) {
        Toast.makeText(getContext(), "Logging in...", Toast.LENGTH_LONG).show();
        chooseWorkoutViewModel.workoutLiveData.removeObservers(this);
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new ReportFragment(report, user)).addToBackStack(null).commit();
    }
}