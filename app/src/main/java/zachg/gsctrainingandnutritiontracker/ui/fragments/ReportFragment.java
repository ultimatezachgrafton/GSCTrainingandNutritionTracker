package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentReportBinding;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.utils.PictureUtils;
import zachg.gsctrainingandnutritiontracker.viewmodels.ReportViewModel;

public class ReportFragment extends Fragment {

    // fragment for Users to fill out their current workout

    private ReportViewModel reportViewModel = new ReportViewModel();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private FragmentReportBinding binding;

    private Button bReport;
    private File photoFile;
    private ImageView photoView;
    private String clientName, dateString;
    private TextView tvClientName, tvDate;

    private Date date;
    private User currentUser = new User();
    private Report currentReport = new Report();

    public ReportFragment() {}

    public ReportFragment(Report report, User user) {
        this.currentReport = report;
        this.currentUser = user;
        this.currentReport.setClientName(user.getClientName());
        this.clientName = currentReport.getClientName();
        this.dateString = report.getDateString();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentReportBinding.inflate(inflater, container, false);
        final View v = binding.getRoot();
        binding.setReport(currentReport);

        FirestoreRepository mRepo = new FirestoreRepository();
        FirestoreRecyclerOptions<Workout> workoutOptions = mRepo.getWorkoutsFromRepo(currentUser);

        reportViewModel = ViewModelProviders.of(getActivity()).get(ReportViewModel.class);
        reportViewModel.init(workoutOptions);

        // TODO: viewModel
        LocalDate date = LocalDate.now();
        dateString = String.valueOf(Calendar.getInstance().getTime());
        String dateFormat = getResources().getString(R.string.date);
        final String dateMsg = String.format(dateFormat, date);
        tvDate.setText(dateMsg);

        updatePhotoView();

        return v;
    }

    private void sendReport() {
        reportViewModel.writeReport(currentReport);
        currentReport.setIsNew(false);
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
        inflater.inflate(R.menu.user_menu, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bViewProfile:
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new ClientProfileFragment(currentUser)).addToBackStack(null).commit();
            case R.id.bInbox:
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new InboxFragment(currentUser)).addToBackStack(null).commit();
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