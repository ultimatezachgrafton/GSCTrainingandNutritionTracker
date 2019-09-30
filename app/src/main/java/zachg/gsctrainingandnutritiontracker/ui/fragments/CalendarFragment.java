package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.os.Bundle;
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
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentCalendarBinding;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.viewmodels.CalendarViewModel;

// The fragment to host the calendar widget to select workout dates

public class CalendarFragment extends Fragment implements ChooseWorkoutFragment.ChooseWorkoutListener {

    FragmentCalendarBinding binding;

    private CalendarViewModel calendarViewModel;
    private CalendarView calendarView;
    private String firstName, greetingFormat, greetingMsg;
    private TextView tvTextView;
    private boolean outdated;

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private Report currentReport = new Report();
    private User currentUser = new User();

    public CalendarFragment(User user) {
        this.currentUser = user;
    }

    public CalendarFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        final View v = binding.getRoot();
        binding.setUser(currentUser);

        calendarViewModel = ViewModelProviders.of(getActivity()).get(CalendarViewModel.class);
        calendarViewModel.init(currentUser);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {

                // sets date format
                final String date = (i1 + 1) + "-" + i2 + "-" + i;

                // If date == null, it cannot be picked
                currentReport = new Report(currentUser, date);
                currentReport.setDateString(date);

                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                       @Override
                       public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                           // TODO: this takes user to workout dialog choice
                           currentReport = new Report(currentUser, date);
                           currentReport.setDateString(date);
                           if (currentReport != null) {
                               chooseWorkoutDialog();
//                               SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
//                                       new ChooseWorkoutFragment(currentUser, currentReport)).addToBackStack(null).commit();
                           }
                       }
                });
            }
        });

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    // Launches ChooseWorkout DialogFragment
    private void chooseWorkoutDialog() {
        FragmentManager fm = getFragmentManager();
        ChooseWorkoutFragment chooseWorkoutFragment = ChooseWorkoutFragment.newInstance();
        // SETS the target fragment for use later when sending results
        chooseWorkoutFragment.setTargetFragment(this, 300);
        chooseWorkoutFragment.show(fm, "fragment_edit_name");
    }

    // Called when the dialog is completed and the results have been passed
    public void onFinishChooseWorkout(int workout) {
        Toast.makeText(getActivity(), "Hi, " + workout, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinishDialog(int workout) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
           inflater.inflate(R.menu.user_menu, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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