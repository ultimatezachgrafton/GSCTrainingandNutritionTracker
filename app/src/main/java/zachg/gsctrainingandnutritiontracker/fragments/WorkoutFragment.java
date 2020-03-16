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

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentWorkoutBinding;
import zachg.gsctrainingandnutritiontracker.models.Exercise;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.viewmodels.WorkoutViewModel;

import static zachg.gsctrainingandnutritiontracker.BR.generatedExerciseWeight;

public class WorkoutFragment extends Fragment {

    private FragmentWorkoutBinding binding;

    private WorkoutViewModel workoutViewModel = new WorkoutViewModel();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private User client = new User();
    private Workout workout = new Workout();
    private ArrayList<Exercise> exercises = new ArrayList<Exercise>();
    private ArrayList<EditText> exerciseNameEditTextArray = new ArrayList<>();
    private ArrayList<EditText> exerciseRepsEditTextArray = new ArrayList<>();
    private ArrayList<EditText> exerciseWeightEditTextArray = new ArrayList<>();

    private int totalExerciseNameEditTexts = 0;
    private int totalExerciseWeightEditTexts = 0;
    private int totalExerciseRepsEditTexts = 0;
    private Button bAddExercise;
    private String workoutTitle, generatedExerciseName, generatedExerciseReps, generatedExerciseWeight;

    private static final int REQUEST_CONTACT = 1;
    public String TAG = "WorkoutFragment";

    public WorkoutFragment() {}

    public WorkoutFragment(Workout workout) {
    }

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
        binding.setClient(client);

        LinearLayout ll = new LinearLayout(getContext());
        ll = v.findViewById(R.id.addEtsLinearLayout);
        binding.setAddEts(ll);

        binding.setGeneratedExerciseName(generatedExerciseName);
        binding.setGeneratedExerciseReps(generatedExerciseReps);
        binding.setGeneratedExerciseWeight(generatedExerciseWeight);

        binding.setModel(workoutViewModel);
        workoutViewModel = ViewModelProviders.of(getActivity()).get(WorkoutViewModel.class);
        workoutViewModel.init(client, workout);

        exercises = workout.getExercises();
        binding.setExercises(exercises);

        while (totalExerciseNameEditTexts < exercises.size() ) {
           addLine(ll);
        }

        return v;

        // TODO: allows editing and deletion of workouts, exercises - put in all observers etc
    }

    public void addLine(LinearLayout ll) {
//         add EditTexts
        EditText et = new EditText(getContext());
        EditText et2 = new EditText(getContext());
        EditText et3 = new EditText(getContext());
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        et.setLayoutParams(p);
        et2.setLayoutParams(p);
        et3.setLayoutParams(p);

        et.setTextColor(Color.WHITE);
        et2.setTextColor(Color.WHITE);
        et3.setTextColor(Color.WHITE);
        et.setText(exercises.get(totalExerciseNameEditTexts).getExerciseName());
        et2.setText(exercises.get(totalExerciseRepsEditTexts).getReps());
        et3.setText(exercises.get(totalExerciseWeightEditTexts).getExerciseWeight());
        et.setId(totalExerciseNameEditTexts + 1);
        et2.setId(totalExerciseRepsEditTexts + 1);
        et3.setId(totalExerciseWeightEditTexts + 1);

        // generate and style TextView
        TextView tv = new TextView(getContext());
        int i = totalExerciseNameEditTexts + 1;
        tv.setText("Exercise " + i + ":");
        tv.setTextColor(Color.WHITE);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setTextSize(18);

        // Add to the View
        ll.addView(tv);
        ll.addView(et);
        ll.addView(et2);
        ll.addView(et3);

        exerciseNameEditTextArray.add(totalExerciseNameEditTexts, et);
        exerciseRepsEditTextArray.add(totalExerciseRepsEditTexts, et2);
        exerciseWeightEditTextArray.add(totalExerciseWeightEditTexts, et3);

        totalExerciseNameEditTexts++;
        totalExerciseRepsEditTexts++;
        totalExerciseWeightEditTexts++;
        Log.d(TAG, "addLine3" + totalExerciseNameEditTexts);
    }


    // should be in ViewModel
    // takes array
    public void getEtValues(Workout workout, int w, User client, LinearLayout ll) {

        ArrayList<Exercise> exArray = new ArrayList<Exercise>();

//        String workoutTitle = workoutTitleEditText.toString;

        // set exerciseName and exerciseReps
        for (int i=0; i < exerciseNameEditTextArray.size(); i++) {
            String exName = exerciseNameEditTextArray.get(i).getText().toString();
            String exReps = exerciseRepsEditTextArray.get(i).getText().toString();
            String exWeight = exerciseWeightEditTextArray.get(i).getText().toString();
            Exercise generatedExercise = new Exercise(exName, exReps, exWeight);
            exArray.add(generatedExercise);
        }

        workout.setExercises(exArray);

        workoutViewModel.updateWorkout(client, workout);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
    }
}
