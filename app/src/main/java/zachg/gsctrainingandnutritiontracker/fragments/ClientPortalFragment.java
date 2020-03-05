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
    private User currentUser = new User();
    private Report currentReport = new Report();
    private ClientPortalViewModel clientProfileViewModel;
    public String TAG = "ClientPortalFragment";
    public CalendarView calendarView;

    public ClientPortalFragment(User user) {
        this.currentUser = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment_report_list
        binding = FragmentClientProfileBinding.inflate(inflater, container, false);
        final View v = binding.getRoot();
        binding.setUser(currentUser);
        binding.setReport(currentReport);
        binding.setFragment(this);
        binding.setViewmodel(clientProfileViewModel);

        clientProfileViewModel = ViewModelProviders.of(this).get(ClientPortalViewModel.class);
        clientProfileViewModel.init(currentUser);

        clientProfileViewModel.reportLiveData.observe(this, new Observer<Report>() {
            @Override
            public void onChanged(Report r) {
                if (!r.isNew()) {
                    goToNewReport();
                } else {
                   goToViewReport();
                }
            }
        });

        // listener explicitly called to address issue that as of this writing android inversebinding
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
            clientProfileViewModel.getReportByUser(currentUser);
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


    public void goToNewReport() {
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new ReportFragment(currentReport, currentUser)).addToBackStack(null).commit();
    }

    public void goToViewReport() {
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new ViewReportFragment(currentUser, currentReport.getDateString())).addToBackStack(null).commit();
    }

}