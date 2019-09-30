package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.DialogFragment;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentChooseWorkoutBinding;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.viewmodels.ChooseWorkoutViewModel;

public class ChooseWorkoutFragment extends DialogFragment implements View.OnClickListener {

    FragmentChooseWorkoutBinding binding;
    ChooseWorkoutViewModel chooseWorkoutViewModel;
    private User user = new User();
    private Report report = new Report();
    private Spinner spinner;
    private String string;
    private Button bSubmit;
    private Workout workout = new Workout();
    private int workoutDay;

    ChooseWorkoutFragment() {}

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

    public static ChooseWorkoutFragment newInstance() {
        ChooseWorkoutFragment frag = new ChooseWorkoutFragment();
        return frag;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentChooseWorkoutBinding.inflate(inflater, container, false);
        final View v = binding.getRoot();

        // TODO: get user
        this.user = user;
        chooseWorkoutViewModel = new ChooseWorkoutViewModel(user);

        binding.setViewModel(chooseWorkoutViewModel);

        return v;
    }

    @Override
    public void onClick(View v) {
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new ReportFragment(report, user)).addToBackStack(null).commit();
    }
}