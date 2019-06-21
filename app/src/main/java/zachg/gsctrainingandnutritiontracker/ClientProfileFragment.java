package zachg.gsctrainingandnutritiontracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import zachg.gsctrainingandnutritiontracker.SingleFragmentActivity;

import androidx.fragment.app.Fragment;

import zachg.gsctrainingandnutritiontracker.inbox.AskBenFragment;

import static zachg.gsctrainingandnutritiontracker.login.LoginHandler.currentUser;

public class ClientProfileFragment extends Fragment implements View.OnClickListener {

    TextView tvClientName;
    Button bToDatePicker;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_client_profile, container, false);
        bToDatePicker = v.findViewById(R.id.bToDatePicker);
        tvClientName = v.findViewById(R.id.tvClientName);
        tvClientName.setText(currentUser.getClientName());

        // gender
        // birthdate
        // date joined
        // admin or user

        // progress chart

        // to client's datepicker
        bToDatePicker.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container, new DatePickerFragment()).addToBackStack(null).commit();
    }
}
