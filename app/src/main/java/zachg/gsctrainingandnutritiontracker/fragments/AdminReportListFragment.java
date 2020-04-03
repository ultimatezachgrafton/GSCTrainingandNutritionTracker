package zachg.gsctrainingandnutritiontracker.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.adapters.ReportListAdapter;
import zachg.gsctrainingandnutritiontracker.adapters.UserListAdapter;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentAdminReportListBinding;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentAdminUserListBinding;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.viewmodels.AdminReportListViewModel;
import zachg.gsctrainingandnutritiontracker.viewmodels.AdminUserListViewModel;

public class AdminReportListFragment extends Fragment {

    private FragmentAdminReportListBinding binding;
    private AdminReportListViewModel adminReportListViewModel;
    private ReportListAdapter reportListAdapter;
    private Toolbar toolbar;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    public String TAG = "AdminReportListFragment";

    private User currentUser = new User();
    private User currentClient = new User();
    private Report report = new Report();

    public AdminReportListFragment(User user, User client) {
        this.currentUser = user;
        this.currentClient = client;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminReportListBinding.inflate(inflater, container, false);
        final View v = binding.getRoot();
        binding.setUser(currentUser);
        binding.setClient(currentClient);

        // Gets ViewModel instance to observe  LiveData
        binding.setModel(adminReportListViewModel);
        adminReportListViewModel = ViewModelProviders.of(getActivity()).get(AdminReportListViewModel.class);
        adminReportListViewModel.init(currentUser, report);

        adminReportListViewModel.getReports().observe(this, new Observer<FirestoreRecyclerOptions<Report>>() {
            @Override
            public void onChanged(@Nullable FirestoreRecyclerOptions<Report> reports) {
                initRecyclerView(reports);
                reportListAdapter.startListening();
            }
        });

        adminReportListViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (!aBoolean) {
                    binding.rvReport.smoothScrollToPosition(adminReportListViewModel.getReports().getValue().getSnapshots().size() - 1);
                }
            }

        });

        return v;
    }

    private void initRecyclerView(FirestoreRecyclerOptions<Report> reports) {
        reportListAdapter = new ReportListAdapter(reports);
        binding.rvReport.setAdapter(reportListAdapter);

        binding.rvReport.setHasFixedSize(true);
        binding.rvReport.setLayoutManager(new LinearLayoutManager(getContext()));

        reportListAdapter.setOnItemClickListener(new ReportListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                report = adminReportListViewModel.onItemClicked(documentSnapshot, position);
                // Goes to client's profile fragment_report_list
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new ViewReportFragment(currentUser, currentClient, report)).addToBackStack(null).commit();
            }
        });
    }

    public void onStop() {
        super.onStop();
        reportListAdapter.stopListening();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "Optionsmenu");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
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
        } return true;
    }

    //TODO
    public void removeObservers() {

    }

}
