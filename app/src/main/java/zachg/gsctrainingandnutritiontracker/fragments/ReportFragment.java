package zachg.gsctrainingandnutritiontracker.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentReportBinding;
import zachg.gsctrainingandnutritiontracker.models.Exercise;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.adapters.ExerciseListAdapter;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.viewmodels.ReportViewModel;

public class ReportFragment extends Fragment {

    // For Users to fill out their workout as they complete it

    private ReportViewModel reportViewModel = new ReportViewModel();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private ExerciseListAdapter exerciseListAdapter;

    private FragmentReportBinding binding;

    private File photoFile;
    private ImageView photoView;
    private String clientName, dateString;
    private TextView tvClientName, tvDate;
    private ArrayList<Workout> workouts = new ArrayList<>();

    private int totalWorkouts = 0;

    private Date date;
    private User currentUser = new User();
    private Report currentReport = new Report();

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
    public String TAG = "ReportFragment";

    public ReportFragment() {}

    public ReportFragment(Report report, User user) {
        this.currentReport = report;
        this.currentUser = user;
        report.setClientName(user.getClientName());
        this.dateString = report.getDateString();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentReportBinding.inflate(inflater, container, false);
        final View v = binding.getRoot();

        LinearLayout ll = new LinearLayout(getContext());
        ll = v.findViewById(R.id.generateTvs);
        binding.setLinearLayout(ll);

        binding.setReport(currentReport);
        binding.setUser(currentUser);

        binding.setModel(reportViewModel);
        reportViewModel = ViewModelProviders.of(getActivity()).get(ReportViewModel.class);
        reportViewModel.init(currentUser, currentReport);

        reportViewModel.getWorkouts().observe(this, new Observer<Workout>() {
            @Override
            public void onChanged(Workout workout) {
                workouts.add(totalWorkouts, workout);
                totalWorkouts++;
            }
        });

        while (totalExerciseNameEditTexts < exercises.size() ) {
            addLine(ll);
        }

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.user_menu, menu);
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

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bInbox:
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:"));
                startActivity(sendIntent);
                return true;
            case R.id.bLogout:
                auth.signOut();
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new LoginFragment()).addToBackStack(null).commit();
                Toast.makeText(getActivity(), "Logged out", Toast.LENGTH_SHORT).show();
                return true;
            // TODO: ask ben and logged out are strings in res
            case R.id.bSelectWorkout:
                reportViewModel.getWorkoutsFromRepo(currentUser);
        } return true;
    }

    public void goToSelectWorkout(User user) {
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new SelectWorkoutFragment(user)).addToBackStack(null).commit();
    }

}