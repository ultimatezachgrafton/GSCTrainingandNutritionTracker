package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.Date;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentViewReportBinding;
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

    private FragmentViewReportBinding binding;

    private File photoFile;
    private ImageView profilePhoto;
    private User currentUser = new User();
    private Report currentReport = new Report();
    private Date date = new Date();

    public ViewReportFragment() {}

    public ViewReportFragment(User user, String dateString) {
        this.date = date;//currentReport = report;
        this.currentUser = user;
    }

    public ViewReportFragment(Report report, User user) {
        this.currentUser = user;
        this.currentReport = report;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentViewReportBinding.inflate(inflater, container, false);
        final View v = binding.getRoot();

        binding.setReport(currentReport);
        binding.setProfilePhoto(profilePhoto);

        adminReportViewModel = ViewModelProviders.of(getActivity()).get(ViewReportViewModel.class);
        adminReportViewModel.init(currentUser, currentReport);
        binding.setReport(adminReportViewModel.getCurrentReport());

        updatePhotoView();

        return v;
    }

    public File getPhotoFile(User user) {
        File filesDir = getContext().getFilesDir();
        return new File( filesDir, user.getPhotoFilename());
    }

    private void updatePhotoView() {
        if (photoFile == null || !photoFile.exists()) {
            profilePhoto.setImageDrawable(null);
            profilePhoto.setContentDescription(
                    getString(R.string.report_photo_no_image_description)
            );
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(photoFile.getPath(), getActivity());
            profilePhoto.setImageBitmap(bitmap);
            profilePhoto.setContentDescription(
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
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:"));
                startActivity(sendIntent);
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