package zachg.gsctrainingandnutritiontracker.ui.fragments;

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

import java.util.Date;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentClientProfileBinding;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.viewmodels.ClientProfileViewModel;

public class ClientProfileFragment extends Fragment {

    FragmentClientProfileBinding binding;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private User currentUser = new User();
    public Date date = new Date();
    private Report currentReport = new Report();
    private ClientProfileViewModel clientProfileViewModel;
    public String TAG = "ClientProfileFragment";
    public CalendarView calendarView;

    public ClientProfileFragment(User user) {
        this.currentUser = user;
        Log.d(TAG, "cpf: " + currentUser.getClientName());
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

        clientProfileViewModel = ViewModelProviders.of(this).get(ClientProfileViewModel.class);
        clientProfileViewModel.init(currentUser);

        clientProfileViewModel.reportLiveData.observe(this, new Observer<Report>() {
            @Override
            public void onChanged(Report r) {
                goToReport();
            }
        });

        binding.calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Log.d(TAG, "aLong: " + new Date(year, month, dayOfMonth).toString());
        });

        //CalendarView calendarView=findViewById(R.id.calendarView);
        //calendarView.setOnDateChangeListener(new OnDateChangeListener() {
//
//            @Override
//            public void onSelectedDayChange(CalendarView view, int year, int month,
//                                            int dayOfMonth) {
//                Toast.makeText(getApplicationContext(), ""+dayOfMonth, 0).show();// TODO Auto-generated method stub
//
//            }
//        });

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

    // change to listener, as android two-way binding is not supported (though it is listed as if it is)
    public void onDateSelected() {
        // TODO: get date from binding adapter, currently gives current date
        Log.d(TAG, "onDateSelected: " + date);
        clientProfileViewModel.getReportByDate(currentUser, date);
    }

    public void goToReport() {
        Log.d(TAG, "report");
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new ReportFragment(currentReport, currentUser)).addToBackStack(null).commit();
    }
}