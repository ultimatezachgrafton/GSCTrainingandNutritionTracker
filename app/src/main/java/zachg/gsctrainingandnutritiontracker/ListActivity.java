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
        setContentView(R.layout.activity_fragment);

        // Fragment transaction
        SingleFragmentActivity.fm = getSupportFragmentManager();
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new ListFragment()).addToBackStack(null).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}