package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.io.File;
import java.util.List;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentAdminClientProfileBinding;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.utils.PictureUtils;
import zachg.gsctrainingandnutritiontracker.viewmodels.AdminClientProfileViewModel;
import zachg.gsctrainingandnutritiontracker.viewmodels.ReportViewModel;

public class AdminClientProfileFragment extends Fragment {

    // enter values, they go to repo
    // Ben overwrites old workouts when he sets new workouts in-app, no need to track workoutday

    FragmentAdminClientProfileBinding binding;

    private AdminClientProfileViewModel adminClientProfileViewModel = new AdminClientProfileViewModel();
    private final int REQUEST_PHOTO = 2;
    private Button bToDatePicker, bAddWorkouts, bWriteWorkouts;
    private File photoFile;
    private ImageView photoView;
    private final String ARG_USER_ID = "user_id";

    private EditText etExerciseName, etExerciseNum, etReps, etDay, etWorkoutNum;
    private String exerciseName, exerciseNum, reps;
    private int workoutCycle, workoutNum;

    private User currentUser = new User();

    public AdminClientProfileFragment() {}

    public AdminClientProfileFragment(User user) {
        this.currentUser = user;
        adminClientProfileViewModel = new AdminClientProfileViewModel(user);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        binding = FragmentAdminClientProfileBinding.inflate(inflater, container, false);
        final View v = binding.getRoot();
        binding.setUser(currentUser);
        final Workout workout = new Workout(currentUser);
        binding.setWorkout(workout);

        adminClientProfileViewModel = ViewModelProviders.of(getActivity()).get(AdminClientProfileViewModel.class);
        adminClientProfileViewModel.init();

        bToDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new AdminCalendarFragment(currentUser)).addToBackStack(null).commit();
            }
        });

        bWriteWorkouts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminClientProfileViewModel.writeToWorkouts(workout);
            }
        });

        adminClientProfileViewModel = ViewModelProviders.of(getActivity()).get(AdminClientProfileViewModel.class);
        photoFile = getPhotoFile(currentUser);
        updatePhotoView();

        return v;
    }

    public File getPhotoFile(User user) {
        File filesDir = getActivity().getFilesDir();
        return new File(filesDir, user.getPhotoFilename());
    }

    public void takePhoto() {
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = FileProvider.getUriForFile(getActivity(),
                "zachg.bensfitnessapp.fileprovider", photoFile);
        captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        List<ResolveInfo> cameraActivities = getActivity().getPackageManager().
                queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo activity : cameraActivities) {
            getActivity().grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }

        startActivityForResult(captureImage, REQUEST_PHOTO);
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
}
