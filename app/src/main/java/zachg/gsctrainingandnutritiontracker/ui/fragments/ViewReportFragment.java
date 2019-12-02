package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.Date;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentAdminReportBinding;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.utils.PictureUtils;
import zachg.gsctrainingandnutritiontracker.viewmodels.ViewReportViewModel;

// TODO: change to dialog fragment_report_list that shows the info

public class ViewReportFragment extends Fragment {

    // fragment_report_list for viewing past reports

    private ViewReportViewModel adminReportViewModel;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private FragmentAdminReportBinding binding;

    private File photoFile;
    private ImageView photoView;
    private User currentUser = new User();
    private Report currentReport = new Report();
    private Date date = new Date();

    public ViewReportFragment() {}

    public ViewReportFragment(Date date, User user) {
        this.date = date;//currentReport = report;
        this.currentUser = user;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentAdminReportBinding.inflate(inflater, container, false);
        final View v = binding.getRoot();
        binding.setReport(currentReport);
        adminReportViewModel = ViewModelProviders.of(getActivity()).get(ViewReportViewModel.class);
        adminReportViewModel.init(currentUser, currentReport);

        // if user has sent a report on this day, it displays the data sent
        updatePhotoView();

        adminReportViewModel.getReport().observe(this, new Observer<Report>() {
            @Override
            public void onChanged(@Nullable Report report) {
                //
            }
        });

        return v;
    }

    private void updatePhotoView() {
        if (photoFile == null || !photoFile.exists()) {
            photoView.setImageDrawable(null);
            photoView.setContentDescription(
                    getString(R.string.report_photo_no_image_description)
            );
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(photoFile.getPath(), getActivity());
            photoView.setImageBitmap(bitmap);
            photoView.setContentDescription(
                    getString(R.string.report_photo_image_description)
            );
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.admin_menu, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bViewProfile:
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new ClientProfileFragment(currentUser)).addToBackStack(null).commit();
            case R.id.bAddNewClient:
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new RegisterFragment()).addToBackStack(null).commit();
                return true;
            case R.id.bInbox:
//                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
//                        new InboxFragment(currentUser)).addToBackStack(null).commit();
                return true;
            case R.id.bLogout:
                auth.signOut();
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new LoginFragment()).addToBackStack(null).commit();
                Toast.makeText(getActivity(), "Logged out", Toast.LENGTH_SHORT).show();
                return true;
        } return super.onOptionsItemSelected(item);
    }
}