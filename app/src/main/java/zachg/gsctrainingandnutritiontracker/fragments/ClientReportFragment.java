package zachg.gsctrainingandnutritiontracker.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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
import zachg.gsctrainingandnutritiontracker.viewmodels.ClientReportViewModel;

public class ClientReportFragment extends Fragment {

    // For Users to fill out their workout as they complete it

    private ClientReportViewModel mClientReportViewModel = new ClientReportViewModel();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private ExerciseListAdapter exerciseListAdapter;

    private FragmentReportBinding binding;

    private File photoFile;
    private ImageView photoView;
    private String clientName, dateString;
    private TextView tvClientName, tvDate;
    private ArrayList<Workout> workouts = new ArrayList<>();
    private Workout workout = new Workout();

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

    public ClientReportFragment() {}

    public ClientReportFragment(Report report, User user) {
        this.currentReport = report;
        this.currentUser = user;
        report.setClientName(user.getClientName());
        this.dateString = report.getDateString();
    }

    public ClientReportFragment(Report report, User user, Workout workout) {
        this.currentReport = report;
        this.workout = workout;
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

        binding.setModel(mClientReportViewModel);
        mClientReportViewModel = ViewModelProviders.of(getActivity()).get(ClientReportViewModel.class);
        mClientReportViewModel.init(currentUser, currentReport, workout);

        mClientReportViewModel.getWorkouts().observe(this, new Observer<Workout>() {
            @Override
            public void onChanged(Workout workout) {
                workouts.add(totalWorkouts, workout);
                totalWorkouts++;
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.user_menu, menu);
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
                goToSelectWorkout(currentUser);
        } return true;
    }

    public void goToSelectWorkout(User user) {
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new ClientSelectWorkoutFragment(user, currentReport)).addToBackStack(null).commit();
    }

}