package zachg.gsctrainingandnutritiontracker.fragments;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.io.File;
import java.util.List;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentAdminClientProfileBinding;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;
import zachg.gsctrainingandnutritiontracker.utils.PictureUtils;
import zachg.gsctrainingandnutritiontracker.viewmodels.AdminClientProfileViewModel;

public class AdminClientProfileFragment extends Fragment {

    // The admin enters workout values and sends them to FirestoreRepository
    FragmentAdminClientProfileBinding binding;

    private AdminClientProfileViewModel adminClientProfileViewModel = new AdminClientProfileViewModel();
    private final String ARG_USER_ID = "user_id";
    private String TAG = "AdminClientProfileFragment";
    private String workoutTitle;
    private User user = new User();
    private User client = new User();
    private Workout workout = new Workout();
    private static final int REQUEST_PHOTO = 2;
    private ImageView profilePhoto;
    private ImageButton bCameraButton;
    private File photoFile;

    public AdminClientProfileFragment() {}

    public AdminClientProfileFragment(User user, User client) {
        this.user = user;
        this.client = client;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment_report_list
        binding = FragmentAdminClientProfileBinding.inflate(inflater, container, false);
        View v = binding.getRoot();
        binding.setFragment(this);
        binding.setUser(user);
        binding.setClient(client);
        binding.setWorkout(workout);
        binding.setWorkoutTitle(workoutTitle);
        workout.setClientName(client.getClientName());

        bCameraButton = v.findViewById(R.id.cameraImageButton);
        photoFile = getPhotoFile(client);

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

        binding.setViewmodel(adminClientProfileViewModel);
        adminClientProfileViewModel = ViewModelProviders.of(this).get(AdminClientProfileViewModel.class);
        adminClientProfileViewModel.init();

        adminClientProfileViewModel.getWorkoutTitleLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    toWorkoutFragment(user, client, s);
                }
            }
        });

        adminClientProfileViewModel.getOnError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

        profilePhoto = v.findViewById(R.id.profilePhotoImageView);
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

    public void verifyWorkoutTitle(User client, String workoutTitle) {
        adminClientProfileViewModel.nullWorkoutTitleCheck(client, workoutTitle);
    }

    public void removeObservers() {
        adminClientProfileViewModel.getWorkoutTitleLiveData().removeObservers(this);
        adminClientProfileViewModel.getOnError().removeObservers(this);
    }

    public void toWorkoutList(User user, User client) {
        removeObservers();
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new SelectWorkoutListFragment(user, client)).addToBackStack(null).commit();
    }

    public void toReportList(User user, User client) {
        removeObservers();
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new AdminReportListFragment(user, client)).addToBackStack(null).commit();
    }

    public void toWorkoutFragment(User user, User client, String workoutTitle) {
        removeObservers();
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new AdminUpdateWorkoutFragment(user, client, workoutTitle)).addToBackStack(null).commit();
    }

    public void logout() {
        FirestoreRepository repo = new FirestoreRepository();
        repo.signOut();SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new LoginFragment()).addToBackStack(null).commit();
    }
}
