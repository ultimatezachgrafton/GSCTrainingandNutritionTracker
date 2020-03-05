package zachg.gsctrainingandnutritiontracker.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.viewmodels.SelectWorkoutViewModel;

import static android.media.CamcorderProfile.get;

public class SelectWorkoutFragment extends Fragment {

    private static final String TAG = "MyCustomDialog";

    public interface OnInputListener{
        void sendInput(String input);
    }

    private SelectWorkoutViewModel selectWorkoutViewModel = new SelectWorkoutViewModel();

    private Workout workout = new Workout();
    private User user = new User();
    private ArrayList<Workout> workouts = new ArrayList<>();
    private ArrayList<TextView> workoutTextViewArray = new ArrayList<>();
    private int totalWorkouts = 0;

    private TextView actionCancel;

    private int totalTvs = 0;

    public SelectWorkoutFragment(User user) {
        this.user = user;
        Log.d(TAG, String.valueOf(user.getEmail()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_workout, container, false);
        actionCancel = view.findViewById(R.id.action_cancel);

        selectWorkoutViewModel.init(user);

        LinearLayout ll = new LinearLayout(getContext());
        ll = view.findViewById(R.id.generateTvs);

        selectWorkoutViewModel.getWorkouts().observe(this, new Observer<Workout>() {
            @Override
            public void onChanged(Workout workout) {
                workouts.add(workout);
                totalTvs++;
            }
        });

        while (workouts.size() < totalTvs) {
            addLine(ll, workout);
        }

        return view;
    }

    public void addLine(LinearLayout ll, Workout workout) {
        TextView tv = new TextView(getContext());
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(p);
        tv.setTextColor(Color.WHITE);
        tv.setText(workouts.get(totalTvs).getWorkoutTitle());
        tv.setId(totalTvs + 1);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setTextSize(18);

        // Add to the View
        ll.addView(tv);

        // set onClick

        workoutTextViewArray.add(totalTvs, tv);
        totalTvs++;
        Log.d(TAG, "addLine3" + totalTvs);
    }
}
