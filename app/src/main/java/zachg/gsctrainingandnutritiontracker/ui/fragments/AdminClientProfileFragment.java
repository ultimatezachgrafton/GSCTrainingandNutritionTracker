package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import zachg.gsctrainingandnutritiontracker.databinding.FragmentAdminClientProfileBinding;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.viewmodels.AdminClientProfileViewModel;

public class AdminClientProfileFragment extends Fragment {

    // Enter workout values and send them to FirestoreRepository

    FragmentAdminClientProfileBinding binding;

    private AdminClientProfileViewModel adminClientProfileViewModel = new AdminClientProfileViewModel();
    private final String ARG_USER_ID = "user_id";

    private User currentUser = new User();

    public AdminClientProfileFragment() {}

    public AdminClientProfileFragment(User user) {
        this.currentUser = user;
        adminClientProfileViewModel = new AdminClientProfileViewModel(user);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment_report_list
        binding = FragmentAdminClientProfileBinding.inflate(inflater, container, false);
        final View v = binding.getRoot();
        binding.setFragment(this);
        binding.setUser(currentUser);
        final Workout workout = new Workout(currentUser);
        binding.setWorkout(workout);
        adminClientProfileViewModel.init();
        binding.setViewmodel(adminClientProfileViewModel);

        return v;
    }

    public void toDatePicker() {
//        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
//                new AdminCalendarFragment(currentUser)).addToBackStack(null).commit();
    }
}
