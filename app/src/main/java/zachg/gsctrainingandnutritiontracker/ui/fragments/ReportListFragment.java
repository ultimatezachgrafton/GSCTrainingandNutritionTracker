package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentAdminUserListBinding;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentReportListBinding;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.ui.adapters.ReportListAdapter;
import zachg.gsctrainingandnutritiontracker.ui.adapters.UserListAdapter;
import zachg.gsctrainingandnutritiontracker.viewmodels.AdminUserListViewModel;
import zachg.gsctrainingandnutritiontracker.viewmodels.ReportListViewModel;

public class ReportListFragment extends Fragment {

    private FragmentReportListBinding binding;
    private ReportListViewModel reportListViewModel;
    private ReportListAdapter reportListAdapter;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    public String TAG = "ReportListFragment";

    User currentUser = new User();
    Report currentReport = new Report();

    public ReportListFragment(User user) {
        this.currentUser = user;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentReportListBinding.inflate(inflater, container, false);
        final View v = binding.getRoot();
        binding.setUser(currentUser);
        binding.setReport(currentReport);

        // Gets ViewModel instance to observe  LiveData
        binding.setModel(reportListViewModel);
        reportListViewModel = ViewModelProviders.of(getActivity()).get(ReportListViewModel.class);
        reportListViewModel.init(currentUser);

        reportListViewModel.getReports().observe(this, new Observer<FirestoreRecyclerOptions<Report>>() {
            @Override
            public void onChanged(@Nullable FirestoreRecyclerOptions<Report> reports) {
                initRecyclerView(reports);
                reportListAdapter.startListening();
            }
        });

        reportListViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (!aBoolean) {
                    binding.rvReport.smoothScrollToPosition(reportListViewModel.getReports().getValue().getSnapshots().size() - 1);
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

        // Note: Listener is explicitly called here to address a binding concern improperly documented
        reportListAdapter.setOnItemClickListener(new ReportListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                currentReport = reportListViewModel.onItemClicked(documentSnapshot, position);
                // Goes to client's profile fragment
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new ReportFragment(currentReport, currentUser)).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
        reportListAdapter.stopListening();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.admin_menu, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bAddNewClient:
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new RegisterFragment()).addToBackStack(null).commit();
                return true;
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
}
