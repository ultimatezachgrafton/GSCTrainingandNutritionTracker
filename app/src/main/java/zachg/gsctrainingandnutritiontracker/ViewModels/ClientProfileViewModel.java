package zachg.gsctrainingandnutritiontracker.ViewModels;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.lifecycle.ViewModel;

import zachg.gsctrainingandnutritiontracker.R;

import static zachg.gsctrainingandnutritiontracker.UI.Fragments.AdminListFragment.currentSelectedUser;

public class ClientProfileViewModel extends ViewModel {

    TextView tvClientName;

    public void onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_client_profile, container, false);
        tvClientName = v.findViewById(R.id.tvClientName);

        // expose data to fragment
        tvClientName.setText(currentSelectedUser.getClientName());
    }
}
