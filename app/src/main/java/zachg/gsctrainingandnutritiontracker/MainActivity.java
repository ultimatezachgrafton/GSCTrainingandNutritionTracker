package zachg.gsctrainingandnutritiontracker;
import androidx.fragment.app.Fragment;

public class MainActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {

        return new ListFragment();
        //LoginFragment();
    }
}