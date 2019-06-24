package zachg.gsctrainingandnutritiontracker;

import android.os.Bundle;
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

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.inbox.AskBenFragment;
import zachg.gsctrainingandnutritiontracker.inbox.InboxFragment;
import zachg.gsctrainingandnutritiontracker.login.LoginFragment;
import zachg.gsctrainingandnutritiontracker.login.RegisterFragment;
import zachg.gsctrainingandnutritiontracker.reports.Report;
import zachg.gsctrainingandnutritiontracker.reports.ReportWorkoutFragment;

import static zachg.gsctrainingandnutritiontracker.login.LoginHandler.currentSelectedUser;
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

                // query ReportFragment to get the getDate() && currentSelectedUser.getUserId() that matches the user's id
                // - which is what I did in LoginFragment -
                // date and userId == date && id;
                // if WorkoutDate == null, start an empty Report
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new ReportWorkoutFragment()).addToBackStack(null).commit();
                // else bring up existing report
//              SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
//                        new ReportFragment()).addToBackStack(null).commit();
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