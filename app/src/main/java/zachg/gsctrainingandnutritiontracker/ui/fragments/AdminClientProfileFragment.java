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
import android.widget.LinearLayout;

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
    private User currentClient = new User();

    private Exercise exercise = new Exercise();
    private Exercise exercise2 = new Exercise();
    private Exercise exercise3 = new Exercise();
    private Exercise exercise4 = new Exercise();
    private Exercise exercise5 = new Exercise();
    private Exercise exercise6 = new Exercise();
    private int totalEditTexts = 0;

    private static final int REQUEST_PHOTO = 2;

    private ImageView profilePhoto;
    private ImageButton bCameraButton;
    private Button bAddExercise;
    private File photoFile;

    public AdminClientProfileFragment() {}

    public AdminClientProfileFragment(User user, User client) {
        this.currentUser = user;
        this.currentClient = client;
    }

    // TODO: sending empty exercises crashes

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment_report_list
        binding = FragmentAdminClientProfileBinding.inflate(inflater, container, false);
        View v = binding.getRoot();
        binding.setFragment(this);
        binding.setUser(currentUser);
        binding.setClient(currentClient);
        bCameraButton = v.findViewById(R.id.bCamera);
        photoFile = getPhotoFile(currentClient);

        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        bCameraButton.setOnClickListener( new View.OnClickListener() {
            @Override public void onClick(View v) {Uri uri = FileProvider.getUriForFile(getActivity(),
                    "zachg.gsctrainingandnutritiontracker.fileprovider", photoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri); List<ResolveInfo> cameraActivities = getActivity().getPackageManager().
                        queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo activity : cameraActivities) {getActivity().grantUriPermission(activity.
                    activityInfo.packageName,uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);}
            startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        LinearLayout ll = new LinearLayout(getContext());
        binding.setAddEts(ll);
        binding.setBAddExercise(bAddExercise);

        binding.setExercise(exercise);
        binding.setExercise(exercise2);
        binding.setExercise(exercise3);
        binding.setExercise(exercise4);
        binding.setExercise(exercise5);
        binding.setExercise(exercise6);
        Workout workout = new Workout(currentClient);
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

        profilePhoto = v.findViewById(R.id.profilePhoto);
        updatePhotoView();

        return v;
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

    public void addLine(LinearLayout ll) {
        Log.d(TAG, "addLine");

        // add edittext
        EditText et = new EditText(getContext());
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        et.setLayoutParams(p);

        // dynamically create a name for the et??

        et.setText("Text");
        et.setId(totalEditTexts + 1);
        ll.addView(et);
        totalEditTexts++;
    }

    public void addExerciseValues(Exercise exercise, Exercise exercise2, Exercise exercise3,
                                  Exercise exercise4, Exercise exercise5, Exercise exercise6,
                                  Exercise exercise7, String workoutDay, String workoutTitle) {
        adminClientProfileViewModel.writeToWorkouts(currentClient, exercise, exercise2, exercise3, exercise4, exercise5,
                exercise6, workoutDay, workoutTitle);
    }

    public void toDatePicker() {
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new ReportListFragment(currentUser, currentClient)).addToBackStack(null).commit();
    }
}
