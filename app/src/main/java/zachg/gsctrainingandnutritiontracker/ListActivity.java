package zachg.gsctrainingandnutritiontracker;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

public class ListActivity extends SingleFragmentActivity {

    protected Fragment createFragment() {
        return new ListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}