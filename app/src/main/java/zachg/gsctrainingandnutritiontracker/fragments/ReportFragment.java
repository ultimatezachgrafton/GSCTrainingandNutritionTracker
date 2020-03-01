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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.Date;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentReportBinding;
import zachg.gsctrainingandnutritiontracker.models.Exercise;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.adapters.ExerciseListAdapter;
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

    private Date date;
    private User currentUser = new User();
    private Report currentReport = new Report();

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
        binding.setReport(currentReport);
        binding.setUser(currentUser);

        binding.setModel(reportViewModel);
        reportViewModel = ViewModelProviders.of(getActivity()).get(ReportViewModel.class);
        reportViewModel.init(currentUser, currentReport);

        reportViewModel.getExercises().observe(this, new Observer<FirestoreRecyclerOptions<Exercise>>() {
            @Override
            public void onChanged(FirestoreRecyclerOptions<Exercise> exercises) {
                initRecyclerView(exercises);
                exerciseListAdapter.startListening();
            }
        });

        reportViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (!aBoolean) {
                    binding.rvExercise.smoothScrollToPosition(reportViewModel.getExercises().getValue().getSnapshots().size() - 1);
                }
            }

        });

        return v;
    }

    private void initRecyclerView(FirestoreRecyclerOptions<Exercise> exercises) {
        exerciseListAdapter = new ExerciseListAdapter(exercises);
        binding.rvExercise.setAdapter(exerciseListAdapter);
        binding.rvExercise.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}