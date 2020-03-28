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
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.activities.LoginActivity;
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
    public ClientPortalFragment(User user, User client) {
        this.user = user;
        this.client = client;
    }

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

        // Observes report returning from repo
        clientProfileViewModel.reportLiveData.observe(this, new Observer<Report>() {
            @Override
            public void onChanged(Report r) {
                currentReport = r;
                goToViewReport(user, client, r);
            }
        });

        // TODO string res
        greeting = "Hi, " + user.getFirstName() + "!";

        // Observes for call to repo validating existence of report
        clientProfileViewModel.doesReportExist.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (!aBoolean) {
                    Log.d(TAG, "noreport firing");
                    goToSelectWorkoutList(user, client, currentReport);
                }
            }
        });

        // NOTE: Listener is explicitly called here to address issue that (as of this writing) android inversebinding
        // is not supported for CalendarView (though it is listed in the documentation as if it is)
        binding.calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            clientProfileViewModel.createDateString(year, month, dayOfMonth);
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
                clearBackStack();
                return true;
            // TODO: ask ben and logged out are strings in res
        } return true;
    }

    private void clearBackStack() {
        Log.d(TAG, "Clearbackstack");
        Toast.makeText(getActivity(), "Logged out", Toast.LENGTH_SHORT).show();
        if (SingleFragmentActivity.fm.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = SingleFragmentActivity.fm.getBackStackEntryAt(0);
            SingleFragmentActivity.fm.popBackStackImmediate(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
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