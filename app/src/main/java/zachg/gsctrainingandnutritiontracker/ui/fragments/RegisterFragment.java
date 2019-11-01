package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.lang.ref.WeakReference;

import zachg.gsctrainingandnutritiontracker.databinding.FragmentRegisterBinding;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.viewmodels.RegisterViewModel;

public class RegisterFragment extends Fragment {

    public RegisterFragment() {}

    private FragmentRegisterBinding binding;
    private RegisterViewModel registerViewModel = new RegisterViewModel();
    private User user = new User();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate and biond the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View v = binding.getRoot();

        // Bind this fragment
        binding.setFragment(this);

        // Bind user
        binding.setUser(user);

        // Bind ViewModel
        binding.setViewModel(registerViewModel);

        // Get ViewModel instance
        registerViewModel = ViewModelProviders.of(getActivity()).get(RegisterViewModel.class);

        return v;
    }

    public void onRegisterClick() {
        //startAsyncTask();
        // Checks if all required fields are filled in
        if (validate()) {
            // Checks if passwords match
            if (!user.getPassword().equals(user.getConfirmPassword())) {
                onError("Passwords entered do not match.");

                // ASYNC

            // Checks if entered email is already in use
            } else if (!registerViewModel.duplicateUserCheck(user.getEmail())) {
                registerViewModel.registerUser(user);
                Log.d("plum", "duplicate false, proceeding " + user.getEmail());
            } else {
                onError("This email is already in use: " + user.getEmail());
            }
        } else {
            onError("Please fill out all fields");
        }
    }

    // Checks if all required fields are filled in
    public boolean validate() {
        if (user.getFirstName() != null && user.getLastName() != null && user.getEmail() != null
                && user.getPassword() != null && user.getConfirmPassword() != null) {
            return true;
        } else {
            return false;
        }
    }

    // Shows error
    public void onError(String e) {
        Toast.makeText(getContext(), e, Toast.LENGTH_SHORT).show();
    }

//    public void startAsyncTask() {
//        RegisterAsyncTask task = new RegisterAsyncTask(this);
//        task.execute();
//    }
//
//    private static class RegisterAsyncTask extends AsyncTask<User, User, User> {
////        private WeakReference<RegisterFragment> fragmentWeakReference;
//        RegisterAsyncTask(RegisterFragment registerFragment) {
////            fragmentWeakReference = new WeakReference<RegisterFragment>(registerFragment);
//        }
//        @Override
//        protected User doInBackground(User... users) {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//    }
}