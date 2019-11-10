package zachg.gsctrainingandnutritiontracker.ui.fragments;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentAdminCalendarBinding;
import zachg.gsctrainingandnutritiontracker.models.CalDate;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.viewmodels.AdminCalendarViewModel;

// The fragment to host the calendar widget to select workout dates

public class AdminCalendarFragment extends Fragment {

    FragmentAdminCalendarBinding binding;

    private AdminCalendarViewModel adminCalendarViewModel;
    private CalendarView adminCalendarView;
    CalDate date = new CalDate();

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private User currentUser = new User();

    public AdminCalendarFragment(User user) {
        adminCalendarViewModel = new AdminCalendarViewModel(user);
        this.currentUser = user;
    }

    public AdminCalendarFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminCalendarBinding.inflate(inflater, container, false);
        final View v = binding.getRoot();
        binding.setUser(currentUser);

        binding.setCaldate(date);

        binding.setFragment(this);
        binding.setViewmodel(adminCalendarViewModel);

//        adminCalendarViewModel = ViewModelProviders.of(getActivity()).get(AdminCalendarViewModel.class);
        adminCalendarViewModel.init();

//        adminCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
//                // sets date format
//                final String date = (i1 + 1) + "-" + i2 + "-" + i;
//                final Report currentReport = new Report(currentUser, date);
//
//                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
//                    @Override
//                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//                        if (currentReport != null) {
//                            SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
//                                    new AdminReportFragment(currentReport, currentUser)).addToBackStack(null).commit();
//                        }
//                    }
//                });
//            }
//        });

        return v;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new InboxFragment(currentUser)).addToBackStack(null).commit();
                return true;
            case R.id.bLogout:
                auth.signOut();
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new LoginFragment()).addToBackStack(null).commit();
                Toast.makeText(getActivity(), "Logged out", Toast.LENGTH_SHORT).show();
                return true;
            //TODO: ask ben and logged out are strings in res
        }
        return true;
    }


    // TODO: when date is changed in CalDate, call this
    public void onDateChanged() {
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new AdminReportFragment(date, currentUser)).addToBackStack(null).commit();
    }
}