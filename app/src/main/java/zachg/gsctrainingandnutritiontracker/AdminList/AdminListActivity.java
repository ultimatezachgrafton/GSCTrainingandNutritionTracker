package zachg.gsctrainingandnutritiontracker.AdminList;

import androidx.fragment.app.Fragment;

import zachg.gsctrainingandnutritiontracker.SingleFragmentActivity;

public class AdminListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new AdminListFragment();
    }
}