package zachg.gsctrainingandnutritiontracker.UI.Fragments;

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

import java.util.ArrayList;
import java.util.Date;

import zachg.gsctrainingandnutritiontracker.Models.Report;
import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.Repositories.FirestoreRepository;
import zachg.gsctrainingandnutritiontracker.UI.Activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.UI.Adapters.DatePickerAdapter;
import zachg.gsctrainingandnutritiontracker.ViewModels.DatePickerViewModel;
import zachg.gsctrainingandnutritiontracker.ViewModels.LoginViewModel;

import static zachg.gsctrainingandnutritiontracker.UI.Fragments.AdminListFragment.currentSelectedUser;

// The fragment to host the calendar widget to select workout dates

public class DatePickerFragment<currentSelectedDated> extends Fragment implements DatePickerAdapter.OnItemClickListener {

    private DatePickerViewModel mDatePickerViewModel;
    private CalendarView mCalendarView;
    private String mFirstName;
    private String mGreetingFormat;
    private String mGreetingMsg;
    private TextView tvTextView;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public FirestoreRepository mRepo = new FirestoreRepository();

    public static ArrayList<Report> mReports = new ArrayList<>();
    public static String currentSelectedDate = new String();
    public static Report currentSelectedReport = new Report();

    public DatePickerFragment() {
        //empty constructor
    }

    public DatePickerFragment(ArrayList<Report> reports) {
        // fill DatePickerFragment with mReports
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_date, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        mDatePickerViewModel = ViewModelProviders.of(getActivity()).get(DatePickerViewModel.class);

        mDatePickerViewModel.init();

        mFirstName = currentSelectedUser.getFirstName();
        // TODO: center greeting
        mGreetingFormat = getResources().getString(R.string.select_date_greeting);
        mGreetingMsg = String.format(mGreetingFormat, mFirstName);
        tvTextView = v.findViewById(R.id.textView);
        tvTextView.setText(mGreetingMsg);

        mCalendarView = v.findViewById(R.id.calendarView);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
                // sets date format
                String date = (i1 + 1) + "/" + i2 + "/" + i;
                Log.d("mReports", "onSelectedDayChange: mm/dd/yyyy: " + date);

                mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                        // search mReports for report that matches user and date
                        Date date = new Date(year - 1900, month, dayOfMonth);
                        currentSelectedDate = DateFormat.format("MM.dd.yy", date).toString();

//                        if (mostRecentReport > currentSelectedDate) {
//                            // this report generates random workout
//                        }

                        // does not let admin click on a client's null date
                        if (currentSelectedUser.getIsAdmin()) {
                            if (currentSelectedReport != null) {
                                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                                        new ReportFragment()).addToBackStack(null).commit();
                            }
                        } else if (currentSelectedReport != null) {
                                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                                    new ReportFragment()).addToBackStack(null).commit();
                        }
                    }
                });

                // observer
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
        LoginViewModel lh = new LoginViewModel();
        if (lh.isAdmin) {
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
                mAuth.signOut();
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new LoginFragment()).addToBackStack(null).commit();
                Toast.makeText(getActivity(), "Logged out", Toast.LENGTH_SHORT).show();
                return true;
        }
        return true;
    }
}