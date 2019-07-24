package zachg.gsctrainingandnutritiontracker.UI.Activities;

import androidx.fragment.app.Fragment;

import zachg.gsctrainingandnutritiontracker.UI.Fragments.DatePickerFragment;

public class DatePickerActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new DatePickerFragment();
    }
}