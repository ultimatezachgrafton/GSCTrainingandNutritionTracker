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
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentClientPortalBinding;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;
import zachg.gsctrainingandnutritiontracker.viewmodels.ClientPortalViewModel;

public class ClientPortalFragment extends Fragment {

    FragmentClientPortalBinding binding;

    private User user = new User();
    private User client = new User();
    private Report currentReport = new Report();
    private ClientPortalViewModel clientProfileViewModel;
    public String TAG = "ClientPortalFragment";
    public CalendarView calendarView;
    private String greeting;
    private String dateString;

    // Initializes client for maneuverability within WorkoutListFragment
    public ClientPortalFragment(User user, User client) {
        this.user = user;
        this.client = client;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment_report_list
        binding = FragmentClientPortalBinding.inflate(inflater, container, false);
        final View v = binding.getRoot();
        binding.setUser(user);
        binding.setReport(currentReport);
        binding.setFragment(this);
        binding.setViewmodel(clientProfileViewModel);

        greeting = "Hi, " + user.getFirstName() + "!";
        binding.setGreeting(greeting);

        clientProfileViewModel = ViewModelProviders.of(this).get(ClientPortalViewModel.class);
        clientProfileViewModel.init(user);

        // Observes report returning from repo
        clientProfileViewModel.getExistingReport().observe(this, new Observer<Report>() {
            @Override
            public void onChanged(Report r) {
                goToViewReport(user, client, r);
            }
        });

        clientProfileViewModel.getOnError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

        // Observes for call to repo validating existence of report
        clientProfileViewModel.getDoesReportExist().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (!aBoolean) {
                    goToSelectWorkoutList(user, client, currentReport);
                }
            }
        });

        // NOTE: Listener is explicitly called here to address issue that (as of this writing) android inversebinding
        // is not supported for CalendarView (though it is listed in the documentation as if it is)
        binding.calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String dayOfMonthStr, monthStr;
            // if dayOfMonth is less than 10, put a zero in front of it
            if (dayOfMonth < 10) {
                dayOfMonthStr = "0" + (dayOfMonth);
            } else {
                dayOfMonthStr = String.valueOf(dayOfMonth);
            }
            if (month < 10) {
                monthStr = "0" + (month + 1);
            } else {
                monthStr = String.valueOf(month);
            }
            String dateString = (monthStr + "-" + dayOfMonthStr + "-" + year);
            binding.setDateString(dateString);
            currentReport.setDateString(dateString);
            clientProfileViewModel.getReportFromRepo(user, dateString);
        });

        return v;
    }

    public void removeObservers() {
        //todo
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void goToSelectWorkoutList(User user, User client, Report report) {
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new SelectWorkoutListFragment(user, client, report)).addToBackStack(null).commit();
    }

    public void goToViewReport(User user, User client, Report report) {
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new ViewReportFragment(user, client, report)).addToBackStack(null).commit();
    }

}