package zachg.gsctrainingandnutritiontracker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

public class DatePickerActivity extends SingleFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment createFragment() {
        return new DatePickerFragment();
    }


}
