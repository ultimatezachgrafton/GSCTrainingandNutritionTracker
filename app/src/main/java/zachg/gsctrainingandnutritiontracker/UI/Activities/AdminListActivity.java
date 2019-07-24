package zachg.gsctrainingandnutritiontracker.UI.Activities;

import androidx.fragment.app.Fragment;

import zachg.gsctrainingandnutritiontracker.UI.Fragments.AdminListFragment;

public class AdminListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new AdminListFragment();
    }
}