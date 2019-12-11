package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
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
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.ui.adapters.WorkoutListAdapter;
import zachg.gsctrainingandnutritiontracker.utils.ContactUtils;
import zachg.gsctrainingandnutritiontracker.viewmodels.ReportViewModel;

public class ReportFragment extends Fragment {

    // For Users to fill out their workout as they complete it

    private ReportViewModel reportViewModel = new ReportViewModel();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private WorkoutListAdapter workoutListAdapter;

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
        reportViewModel.init(currentUser);

        reportViewModel.getWorkouts().observe(this, new Observer<FirestoreRecyclerOptions<Workout>>() {
            @Override
            public void onChanged(FirestoreRecyclerOptions<Workout> workouts) {
                initRecyclerView(workouts);
                workoutListAdapter.startListening();
            }
        });

        reportViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (!aBoolean) {
                    binding.rvWorkout.smoothScrollToPosition(reportViewModel.getWorkouts().getValue().getSnapshots().size() - 1);
                }
            }

        });

        // TODO: add to util
//        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//        startActivityForResult(pickContact, REQUEST_CONTACT);
//        pickContact.addCategory(Intent.CATEGORY_HOME);
//        PackageManager packageManager = getActivity().getPackageManager();
//        if (packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null) {  }

        return v;
    }

    private void initRecyclerView(FirestoreRecyclerOptions<Workout> workouts) {
        workoutListAdapter = new WorkoutListAdapter(workouts);
        binding.rvWorkout.setAdapter(workoutListAdapter);
        binding.rvWorkout.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
        workoutListAdapter.stopListening();
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
            case R.id.bInbox: ;
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("smsto:" + currentUser.getPhoneNumber()));
                startActivity(sendIntent);
                return true;
            case R.id.bLogout:
                auth.signOut();
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new LoginFragment()).addToBackStack(null).commit();
                Toast.makeText(getActivity(), "Logged out", Toast.LENGTH_SHORT).show();
                return true;
        } return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            // Specify which fields you want your query to return values for
            String[] queryFields = new String[]{ContactsContract.Contacts.DISPLAY_NAME};
            // Perform your query - the contactUri is like a "where" clause here
            Cursor c = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);
            try {
                // Double-check that you actually got results
                if (c.getCount() == 0) {
                    return;
                }
                // Pull out the first column of the first row of data - that is your suspect's name
                c.moveToFirst();
                String suspect = c.getString(0);
            } finally {
                c.close();
            }
        }
    }
}