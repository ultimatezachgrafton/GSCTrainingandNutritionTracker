package zachg.gsctrainingandnutritiontracker.calendar;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;

import zachg.gsctrainingandnutritiontracker.AdminList.UserListAdapter;
import zachg.gsctrainingandnutritiontracker.ClientProfileFragment;
import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.inbox.AskBenFragment;
import zachg.gsctrainingandnutritiontracker.inbox.InboxFragment;
import zachg.gsctrainingandnutritiontracker.login.LoginFragment;
import zachg.gsctrainingandnutritiontracker.login.RegisterFragment;
import zachg.gsctrainingandnutritiontracker.reports.Report;
import zachg.gsctrainingandnutritiontracker.reports.ReportHandler;
import zachg.gsctrainingandnutritiontracker.reports.ReportWorkoutFragment;

import static zachg.gsctrainingandnutritiontracker.AdminList.AdminListFragment.currentSelectedUser;
import static zachg.gsctrainingandnutritiontracker.login.LoginHandler.currentUser;
import static zachg.gsctrainingandnutritiontracker.login.LoginHandler.isAdmin;

// The fragment to host the calendar widget to select workout dates

public class DatePickerFragment extends Fragment {

    public static final String EXTRA_DATE = "zachg.gsctrainingandnutritiontracker.date";
    private static final String ARG_DATE = "date";
    private static final String TAG = "CalendarFragment";
    private CalendarView mCalendarView;
    private String mFirstName;
    private String mGreetingFormat;
    private String mGreetingMsg;
    private TextView tvTextView;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ArrayList<Report> mReports = new ArrayList<>();

    public static Report currentSelectedReport;

    public DatePickerFragment() {
        //empty constructor
    }

    public DatePickerFragment(ArrayList<Report> mReports) {
        // fill DatePickerFragment with mReports
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_date, container, false);

        // get cSU by user's name
        mFirstName = currentSelectedUser.getFirstName();
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
                Log.d(TAG, "onSelectedDayChange: mm/dd/yyyy: " + date);

                mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                        // search mReports for report that matches user and date
                        Date date = new Date(year - 1900, month, dayOfMonth);
                        String currentSelectedDate = DateFormat.format("MM.dd.yy", date).toString();

                        ReportHandler.fetchReportsByUserDate(mReports, currentSelectedUser.getClientName(), currentSelectedDate);

                        if (mReports != null) {
                            SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                                    new ReportWorkoutFragment()).addToBackStack(null).commit();
                        }
                    }
                });
            }
        });

        return v;
    }

    public Report getReportAtPosition(int position) {
        currentSelectedReport = mReports.get(position);
        Log.d("currentSelectedReport", String.valueOf(currentSelectedReport));
        return currentSelectedReport;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (isAdmin) {
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