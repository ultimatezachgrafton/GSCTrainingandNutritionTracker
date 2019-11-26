package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentClientProfileBinding;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.viewmodels.ClientProfileViewModel;

public class ClientProfileFragment extends Fragment {

    FragmentClientProfileBinding binding;
    private ArrayList<String> workoutTitleArray = new ArrayList<>();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private User currentUser = new User();
    Date date = new Date();
    private Report currentReport = new Report();
    private ClientProfileViewModel clientProfileViewModel;
    public String TAG = "ClientProfileFragment";

    public ClientProfileFragment(User user) {
        this.currentUser = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        binding = FragmentClientProfileBinding.inflate(inflater, container, false);
        final View v = binding.getRoot();

        binding.setUser(currentUser);

        binding.setDate(date);

        binding.setReport(currentReport);

        binding.setFragment(this);

        binding.setViewmodel(clientProfileViewModel);
        clientProfileViewModel = ViewModelProviders.of(this).get(ClientProfileViewModel.class);
        clientProfileViewModel.init(currentUser);

        clientProfileViewModel.dateLiveData.observe(this, new Observer<Date>() {
            @Override
            public void onChanged(Date d) {
                if (d == null) {
                    Toast.makeText(getContext(), "That date does not exist.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "date changed");
                    date = d;
                }
            }
        });

        // TODO: set live data and observer for Report
        clientProfileViewModel.reportLiveData.observe(this, new Observer<Report>() {
            @Override
            public void onChanged(Report r) {
                if (r == null) {
                    Toast.makeText(getContext(), "That report does not exist.", Toast.LENGTH_SHORT).show();
                } else {
                    currentReport = r;
                    Log.d(TAG, "report changed");
                }
            }
        });

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.user_menu, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bInbox:
                // TODO: go to texting interface
//                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
//                        new InboxFragment(currentUser)).addToBackStack(null).commit();
                return true;
            case R.id.bLogout:
                auth.signOut();
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new LoginFragment()).addToBackStack(null).commit();
                Toast.makeText(getActivity(), "Logged out", Toast.LENGTH_SHORT).show();
                return true;
            // TODO: ask ben and logged out are strings in res
        } return true;
    }

    public void onDateSelected(User user, Date date) {
        clientProfileViewModel.getReportByDate(user, date);
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new ReportFragment(currentReport, currentUser)).addToBackStack(null).commit();
    }

}