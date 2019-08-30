package zachg.gsctrainingandnutritiontracker.ui.activities;

import androidx.fragment.app.Fragment;

import zachg.gsctrainingandnutritiontracker.ui.fragments.AdminListFragment;

public class AdminListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new AdminListFragment();
    }
}