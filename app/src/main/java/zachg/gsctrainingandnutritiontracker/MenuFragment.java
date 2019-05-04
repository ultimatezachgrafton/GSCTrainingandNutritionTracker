package zachg.gsctrainingandnutritiontracker;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.fragment.app.Fragment;

public class MenuFragment extends Fragment {

    MenuItem fav;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        fav = menu.add("add");
        fav.setIcon(R.drawable.ic_menu_add);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
