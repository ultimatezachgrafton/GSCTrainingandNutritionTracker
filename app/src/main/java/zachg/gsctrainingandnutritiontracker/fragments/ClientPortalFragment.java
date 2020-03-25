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
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentClientProfileBinding;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.viewmodels.ClientPortalViewModel;

public class ClientPortalFragment extends Fragment {

    FragmentClientProfileBinding binding;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private User user = new User();
    private User client = new User();
    private Report currentReport = new Report();
    private ClientPortalViewModel clientProfileViewModel;
    public String TAG = "ClientPortalFragment";
    public CalendarView calendarView;
    private String greeting;

    // Initializes client for maneuverability within WorkoutListFragment
    public ClientPortalFragment(User user, User client) { this.user = user; this.client = client; }

    public ClientPortalFragment(User user) { this.user = user; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment_report_list
        binding = FragmentClientProfileBinding.inflate(inflater, container, false);
        final View v = binding.getRoot();
        binding.setUser(user);
        binding.setReport(currentReport);
        binding.setFragment(this);
        binding.setViewmodel(clientProfileViewModel);
        binding.setGreeting(greeting);

        clientProfileViewModel = ViewModelProviders.of(this).get(ClientPortalViewModel.class);
        clientProfileViewModel.init(user);

        clientProfileViewModel.reportLiveData.observe(this, new Observer<Report>() {
            @Override
            public void onChanged(Report r) {
                currentReport = r;
            }
        });

        // TODO string res
        greeting = "Hi, " + user.getFirstName() + "!";

        clientProfileViewModel.noReport.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer i) {
                goToViewReport(currentReport);
            }
        });

        // NOTE: Listener is explicitly called to address issue that (as of this writing) android inversebinding
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
            currentReport.setDateString(dateString);

            if (currentReport == null) {
                currentReport.setIsNew(true);
                goToSelectWorkoutList(user, client, currentReport);
            } else {
                goToViewReport(currentReport);
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
        } return true;
    }


    public void goToSelectWorkoutList(User user, User client, Report report) {
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new SelectWorkoutListFragment(user, client, report)).addToBackStack(null).commit();
    }

    public void goToViewReport(Report report) {
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new ViewReportFragment(user, client, report)).addToBackStack(null).commit();
    }

}