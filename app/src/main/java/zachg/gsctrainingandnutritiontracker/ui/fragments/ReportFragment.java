package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.ui.adapters.WorkoutListAdapter;
import zachg.gsctrainingandnutritiontracker.viewmodels.ReportViewModel;

public class ReportFragment extends Fragment {

    // fragment_report_list for Users to fill out their current workout

    private ReportViewModel reportViewModel = new ReportViewModel();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private WorkoutListAdapter workoutListAdapter;

    private FragmentReportBinding binding;

    private Button bReport;
    private File photoFile;
    private ImageView photoView;
    private String clientName, dateString;
    private TextView tvClientName, tvDate;

    private Date date;
    private User currentUser = new User();
    private Report currentReport = new Report();

    public String TAG = "ReportFragment";

    public ReportFragment() {}

    public ReportFragment(Report report, User user) {
        this.currentReport = report;
        this.currentUser = user;
        this.currentReport.setClientName(user.getClientName());
        this.clientName = currentReport.getClientName();
//        this.date = report.getDate();
//        Log.d(TAG, String.valueOf(date));
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

        binding.setReportViewModel(reportViewModel);
        reportViewModel = ViewModelProviders.of(getActivity()).get(ReportViewModel.class);
        reportViewModel.init(currentUser, currentReport);

        reportViewModel.getWorkouts(currentUser).observe(this, new Observer<FirestoreRecyclerOptions<Workout>>() {
            @Override
            public void onChanged(FirestoreRecyclerOptions<Workout> workouts) {
                initRecyclerView(workouts);
                workoutListAdapter.startListening();
            }
        });

//        reportViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
//            @Override
//            public void onChanged(@Nullable Boolean aBoolean) {
//                if (!aBoolean) {
//                    binding.rvWorkout.smoothScrollToPosition(reportViewModel.getWorkouts().getValue().getSnapshots().size() - 1);
//                }
//            }
//
//        });

        return v;
    }

    private void initRecyclerView(FirestoreRecyclerOptions<Workout> workouts) {
        workoutListAdapter = new WorkoutListAdapter(workouts);
        binding.rvWorkout.setAdapter(workoutListAdapter);

        binding.rvWorkout.setHasFixedSize(true);
        binding.rvWorkout.setLayoutManager(new LinearLayoutManager(getContext()));

//        workoutListAdapter.setOnItemClickListener(new UserListAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
//               // currentUser = reportViewModel.onItemClicked(documentSnapshot, position);
//                // Goes to client's profile fragment_report_list
//                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
//                        new AdminClientProfileFragment(currentUser)).addToBackStack(null).commit();
//            }
//        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
        workoutListAdapter.stopListening();
    }

    public void sendReport() {
        Log.d(TAG, "send Report");
        reportViewModel.writeReport(currentReport);
        currentReport.setIsNew(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.user_menu, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bViewProfile:
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new ClientProfileFragment(currentUser)).addToBackStack(null).commit();
            case R.id.bInbox:
//                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
//                        new InboxFragment(currentUser)).addToBackStack(null).commit();
                return true;
            case R.id.bLogout:
                auth.signOut();
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new LoginFragment()).addToBackStack(null).commit();
                Toast.makeText(getActivity(), "Logged out", Toast.LENGTH_SHORT).show();
                return true;
        } return super.onOptionsItemSelected(item);
    }
}