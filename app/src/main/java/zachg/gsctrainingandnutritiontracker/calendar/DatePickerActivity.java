package zachg.gsctrainingandnutritiontracker.calendar;

import androidx.fragment.app.Fragment;

import zachg.gsctrainingandnutritiontracker.SingleFragmentActivity;

public class DatePickerActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new DatePickerFragment();
    }
}