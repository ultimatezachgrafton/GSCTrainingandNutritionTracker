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
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.io.File;
import java.util.List;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentAdminClientProfileBinding;
import zachg.gsctrainingandnutritiontracker.models.Exercise;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.utils.PictureUtils;
import zachg.gsctrainingandnutritiontracker.viewmodels.AdminClientProfileViewModel;
import zachg.gsctrainingandnutritiontracker.viewmodels.RegisterViewModel;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AdminClientProfileFragment extends Fragment {

    // The admin enters workout values and sends them to FirestoreRepository

    FragmentAdminClientProfileBinding binding;

    private AdminClientProfileViewModel adminClientProfileViewModel = new AdminClientProfileViewModel();
    private final String ARG_USER_ID = "user_id";

    private User currentUser = new User();
    private Exercise exercise = new Exercise();
    private Exercise exercise2 = new Exercise();
    private Exercise exercise3 = new Exercise();
    private Exercise exercise4 = new Exercise();
    private Exercise exercise5 = new Exercise();
    private Exercise exercise6 = new Exercise();
    private Exercise exercise7 = new Exercise();
    private int totalEditTexts;

    private static final int REQUEST_PHOTO = 2;

    private ImageView profilePhoto;
    private ImageButton bCamera;
    private File photoFile;

    public AdminClientProfileFragment() {}

    public AdminClientProfileFragment(User user) {
        this.currentUser = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment_report_list
        binding = FragmentAdminClientProfileBinding.inflate(inflater, container, false);
        View v = binding.getRoot();
        binding.setFragment(this);
        binding.setUser(currentUser);
        binding.setProfilePhoto(profilePhoto);
        binding.setBCamera(bCamera);

        PackageManager packageManager = getActivity().getPackageManager();

        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = photoFile != null && captureImage.resolveActivity(packageManager) != null;
        bCamera.setEnabled(canTakePhoto);
        bCamera.setOnClickListener( new View.OnClickListener() {
            @Override public void onClick(View v) { Uri uri = FileProvider.getUriForFile(getActivity(),
                    "zachg.gsctrainingandnutritiontracker.fileprovider", photoFile);
            captureImage.putExtra( MediaStore.EXTRA_OUTPUT, uri); List<ResolveInfo> cameraActivities = getActivity().getPackageManager().
                        queryIntentActivities( captureImage, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo activity : cameraActivities) {getActivity().grantUriPermission(activity.
                    activityInfo.packageName,uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);}
            startActivityForResult( captureImage, REQUEST_PHOTO); } });

        binding.setExercise(exercise);
        binding.setExercise(exercise2);
        binding.setExercise(exercise3);
        binding.setExercise(exercise4);
        binding.setExercise(exercise5);
        binding.setExercise(exercise6);
        binding.setExercise(exercise7);
        Workout workout = new Workout(currentUser);
        binding.setWorkout(workout);

        binding.setViewmodel(adminClientProfileViewModel);
        adminClientProfileViewModel = ViewModelProviders.of(this).get(AdminClientProfileViewModel.class);
        adminClientProfileViewModel.init();

        adminClientProfileViewModel.newExercise.observe(this, new Observer<Exercise>() {
            @Override
            public void onChanged(Exercise exercise) {
                    Log.d(TAG, "changed ex");
            }
        });

        adminClientProfileViewModel.newExercise2.observe(this, new Observer<Exercise>() {
            @Override
            public void onChanged(Exercise exercise) {
                Log.d(TAG, "changed ex");
            }
        });

        adminClientProfileViewModel.newExercise3.observe(this, new Observer<Exercise>() {
            @Override
            public void onChanged(Exercise exercise) {
                Log.d(TAG, "changed ex");
            }
        });

        adminClientProfileViewModel.newExercise4.observe(this, new Observer<Exercise>() {
            @Override
            public void onChanged(Exercise exercise) {
                Log.d(TAG, "changed ex");
            }
        });

        adminClientProfileViewModel.newExercise5.observe(this, new Observer<Exercise>() {
            @Override
            public void onChanged(Exercise exercise) {
                Log.d(TAG, "changed ex");
            }
        });

        adminClientProfileViewModel.newExercise6.observe(this, new Observer<Exercise>() {
            @Override
            public void onChanged(Exercise exercise) {
                Log.d(TAG, "changed ex");
            }
        });

        adminClientProfileViewModel.newExercise7.observe(this, new Observer<Exercise>() {
            @Override
            public void onChanged(Exercise exercise) {
                Log.d(TAG, "changed ex");
            }
        });

        adminClientProfileViewModel.newWorkout.observe(this, new Observer<Workout>() {
            @Override
            public void onChanged(Workout workout) {
                Log.d(TAG, "changed ex");
            }
        });

        updatePhotoView();

        return v;
    }

    public void createExerciseArray() {

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


    public void addExerciseValues(Exercise exercise, Exercise exercise2, Exercise exercise3,
                                  Exercise exercise4, Exercise exercise5, Exercise exercise6,
                                  Exercise exercise7, String workoutDay, String workoutTitle) {
        adminClientProfileViewModel.writeToWorkouts(currentUser, exercise, exercise2, exercise3, exercise4, exercise5,
                exercise6, exercise7, workoutDay, workoutTitle);
    }

    public void toDatePicker() {
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new ReportListFragment(currentUser)).addToBackStack(null).commit();
    }
}
