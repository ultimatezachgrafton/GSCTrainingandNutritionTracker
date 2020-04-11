package zachg.gsctrainingandnutritiontracker.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.fragments.LoginFragment;

// Controls the various fragments

public abstract class SingleFragmentActivity extends AppCompatActivity {
    public static FragmentManager fm;
    protected abstract Fragment createFragment();

    private static final String TAG = "SingleFragmentActivity";

    // Returns ID of layout that the activity will inflate
    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        setSupportActionBar(findViewById(R.id.toolbar));
        fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            fm.beginTransaction().add(R.id.fragment_container, new LoginFragment()).commit();
        }
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //TODO - do all your stuff for when you click on the options item here
        return super.onOptionsItemSelected(item);
    }
}