package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.viewmodels.DatePickerViewModel;

// The fragment to host the calendar widget to select workout dates

public class DatePickerFragment extends Fragment {

    private DatePickerViewModel datePickerViewModel;
    private CalendarView calendarView;
    private String firstName, greetingFormat, greetingMsg;
    private TextView tvTextView;

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private Report currentReport = new Report();
    private User currentUser = new User();

    public DatePickerFragment() {
        //empty required constructor
    }

    public DatePickerFragment(User user) {
        currentUser = user;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_date, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        datePickerViewModel = ViewModelProviders.of(getActivity()).get(DatePickerViewModel.class);
        datePickerViewModel.init(currentUser);

        createGreeting(v);

        calendarView = v.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
                // sets date format
                String date = (i1 + 1) + "/" + i2 + "/" + i;

                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                        // search Reports for report that matches user and date
                        LocalDate date = LocalDate.now();
                        currentReport = new Report(currentUser, date);

                        if (currentReport != null) {
                            SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                                    new ReportFragment(currentReport, currentUser)).addToBackStack(null).commit();
                        }
                    }
                });

                // observer
            }
        });

        return v;
    }

    private void createGreeting(View v) {
        firstName = currentUser.getFirstName();
        greetingFormat = getResources().getString(R.string.select_date_greeting);
        greetingMsg = String.format(greetingFormat, firstName);
        tvTextView = v.findViewById(R.id.tv_select_date);
        tvTextView.setText(R.string.select_date);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (currentUser.getIsAdmin()) {
            inflater.inflate(R.menu.admin_menu, menu);
        } else {
           inflater.inflate(R.menu.user_menu, menu);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bAskBen:
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new AskBenFragment()).addToBackStack(null).commit();
                Toast.makeText(getActivity(), "Ask Ben", Toast.LENGTH_LONG).show();
                return true;
            case R.id.bAddNewClient:
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new RegisterFragment()).addToBackStack(null).commit();
                return true;
            case R.id.bInbox:
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new InboxFragment()).addToBackStack(null).commit();
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
}