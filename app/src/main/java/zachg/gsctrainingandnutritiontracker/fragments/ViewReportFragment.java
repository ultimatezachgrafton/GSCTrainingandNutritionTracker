package zachg.gsctrainingandnutritiontracker.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.Date;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.adapters.ExerciseForViewOnlyListAdapter;
import zachg.gsctrainingandnutritiontracker.adapters.ExerciseListAdapter;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentViewReportBinding;
import zachg.gsctrainingandnutritiontracker.models.Exercise;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.utils.PictureUtils;
import zachg.gsctrainingandnutritiontracker.viewmodels.ViewReportViewModel;

import static androidx.constraintlayout.widget.Constraints.TAG;

// TODO: change to dialog fragment_report_list that shows the info

public class ViewReportFragment extends Fragment {

    // fragment_report_list for viewing past reports
    private ViewReportViewModel viewReportViewModel;

    private ExerciseForViewOnlyListAdapter exerciseListAdapter;

    private FragmentViewReportBinding binding;

    private File photoFile;
    private ImageView profilePhoto;
    private User user = new User();
    private User client = new User();
    private Report report = new Report();
    private Workout workout = new Workout();
    private Exercise exercise = new Exercise();
    private Date date = new Date();

    // TODO fix dailyweightstring "Today's weight" showing up twice
    private String dailyWeightString;

    public ViewReportFragment() {}

    public ViewReportFragment(User user, User client, Report report) {
        this.report = report;
        this.user = user;
        this.client = client;
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

        binding.setFragment(this);
        binding.setUser(user);
        binding.setClient(client);
        binding.setReport(report);
        binding.setExercise(exercise);
        photoFile = getPhotoFile(client);
        binding.setDailyWeightString(report.getDailyWeightString());

        viewReportViewModel = ViewModelProviders.of(getActivity()).get(ViewReportViewModel.class);
        viewReportViewModel.init(user, client, report);

        profilePhoto = v.findViewById(R.id.profilePhotoImageView);
        updatePhotoView();

        viewReportViewModel.getExerciseLiveData().observe(this, new Observer<FirestoreRecyclerOptions<Exercise>>() {
            @Override
            public void onChanged(FirestoreRecyclerOptions<Exercise> e) {
                initRecyclerView(e);
                exerciseListAdapter.startListening();
            }
        });

        viewReportViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (!aBoolean) {
                    binding.rvExercise.smoothScrollToPosition(viewReportViewModel.getExerciseLiveData().getValue().getSnapshots().size() - 1);
                }
            }
        });

        return v;
    }

    private void initRecyclerView(FirestoreRecyclerOptions<Exercise> exercises) {
        exerciseListAdapter = new ExerciseForViewOnlyListAdapter(exercises);
        binding.rvExercise.setAdapter(exerciseListAdapter);
        binding.rvExercise.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public File getPhotoFile(User user) {
        File filesDir = getContext().getFilesDir();
        return new File(filesDir, user.getPhotoFilename());
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
            Log.d(TAG,"bitmap set: " + photoFile.getPath());
            profilePhoto.setContentDescription(
                    getString(R.string.report_photo_image_description)
            );
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bInbox:
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:"));
                startActivity(sendIntent);
                return true;
            case R.id.bLogout:
//                auth.signOut();
                clearBackStack();
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new LoginFragment()).addToBackStack(null).commit();
                Toast.makeText(getActivity(), "Logged out", Toast.LENGTH_SHORT).show();
                return true;
        } return super.onOptionsItemSelected(item);
    }

    private void clearBackStack() {
        if (SingleFragmentActivity.fm.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = SingleFragmentActivity.fm.getBackStackEntryAt(0);
            SingleFragmentActivity.fm.popBackStackImmediate(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
}