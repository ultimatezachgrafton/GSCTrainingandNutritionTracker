package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentClientProfileBinding;
import zachg.gsctrainingandnutritiontracker.models.CalDate;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.viewmodels.ChooseWorkoutViewModel;
import zachg.gsctrainingandnutritiontracker.viewmodels.ClientProfileViewModel;

public class ClientProfileFragment extends Fragment implements ChooseWorkoutFragment.ChooseWorkoutListener {

    FragmentClientProfileBinding binding;
    private ArrayList<String> workoutTitleArray = new ArrayList<>();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private User currentUser = new User();
    private ClientProfileViewModel clientProfileViewModel;
    public String TAG = "ClientProfileFragment";

    public ClientProfileFragment(User user) {
        this.currentUser = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        binding = FragmentClientProfileBinding.inflate(inflater, container, false);
        final View v = binding.getRoot();

        binding.setUser(currentUser);

        CalDate date = new CalDate();
        binding.setCaldate(date);

        binding.setFragment(this);

        binding.setViewmodel(clientProfileViewModel);
        clientProfileViewModel = ViewModelProviders.of(this).get(ClientProfileViewModel.class);
        clientProfileViewModel.init(currentUser);

        clientProfileViewModel.workoutLiveData.observe(this, new Observer<ArrayList<Workout>>() {
            @Override
            public void onChanged(ArrayList<Workout> array) {
                if (array == null) {
                    Toast.makeText(getContext(), "That array does not exist.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "array time");
                    chooseWorkoutDialog();
                }
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
        ChooseWorkoutFragment chooseWorkoutFragment = ChooseWorkoutFragment.newInstance(currentUser);
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
                        new InboxFragment(currentUser)).addToBackStack(null).commit();
                return true;
            case R.id.bLogout:
                auth.signOut();
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new LoginFragment()).addToBackStack(null).commit();
                Toast.makeText(getActivity(), "Logged out", Toast.LENGTH_SHORT).show();
                return true;
            //TODO: ask ben and logged out are strings in res
        } return true;
    }

    public void onSelectWorkoutClick() {
        clientProfileViewModel.getWorkouts(currentUser);
    }

    public void onSelectReportClick() {
        // go to report
    }

    public ArrayList<String> getWorkoutTitleArray() {
        return workoutTitleArray;
    }

}