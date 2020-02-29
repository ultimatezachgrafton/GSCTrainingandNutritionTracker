package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import java.util.ArrayList;
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
    private String TAG = "AdminClientProfileFragment";

    private User currentUser = new User();
    private User currentClient = new User();

    private String workoutTitle, workoutDay, exerciseName, exerciseReps;
    private String exerciseName2, exerciseReps2, exerciseName3, exerciseReps3, exerciseName4,
            exerciseReps4, exerciseName5, exerciseReps5, generatedExerciseName, generatedExerciseReps;
    private ArrayList<EditText> exerciseNameEditTextArray = new ArrayList<>();
    private ArrayList<EditText> exerciseRepsEditTextArray = new ArrayList<>();
    private ArrayList<Exercise> generatedExerciseArray = new ArrayList<>();
    private Exercise exercise = new Exercise();
    private Exercise exercise2 = new Exercise();
    private Exercise exercise3 = new Exercise();
    private Exercise exercise4 = new Exercise();
    private Exercise exercise5 = new Exercise();
    private int totalExerciseNameEditTexts = 5;
    private int totalExerciseRepsEditTexts = 5;

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

        binding.setWorkoutTitle(workoutTitle);
        binding.setWorkoutDay(workoutDay);

        binding.setGeneratedExerciseName(generatedExerciseName);
        binding.setGeneratedExerciseReps(generatedExerciseReps);

        bCameraButton = v.findViewById(R.id.bCamera);
        photoFile = getPhotoFile(currentClient);

        // initialize array values
        EditText et = new EditText(getContext());
        while(exerciseNameEditTextArray.size() < totalExerciseNameEditTexts) exerciseNameEditTextArray.add(0, et);
        while(exerciseRepsEditTextArray.size() < totalExerciseRepsEditTexts) exerciseRepsEditTextArray.add(0, et);

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
        binding.setLl(ll);
        binding.setAddEts(ll);
        binding.setBAddExercise(bAddExercise);

        binding.setExerciseName(exerciseName);
        binding.setExerciseName(exerciseName2);
        binding.setExerciseName(exerciseName3);
        binding.setExerciseName(exerciseName4);
        binding.setExerciseName(exerciseName5);

        binding.setExerciseReps(exerciseReps);
        binding.setExerciseReps2(exerciseReps2);
        binding.setExerciseReps3(exerciseReps3);
        binding.setExerciseReps4(exerciseReps4);
        binding.setExerciseReps5(exerciseReps5);

        Workout workout = new Workout(currentClient);
        binding.setWorkout(workout);

        binding.setViewmodel(adminClientProfileViewModel);
        adminClientProfileViewModel = ViewModelProviders.of(this).get(AdminClientProfileViewModel.class);
        adminClientProfileViewModel.init();

        adminClientProfileViewModel.workoutTitleLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String str) {
                workoutTitle = str;
            }
        });

        adminClientProfileViewModel.workoutDayLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String str) {
                workoutDay = str;
            }
        });

        adminClientProfileViewModel.newExerciseName.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String str) {
                exerciseName = str;
            }
        });

        adminClientProfileViewModel.newExerciseReps.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String str) {
                exerciseReps = str;
            }
        });

        adminClientProfileViewModel.newExerciseName2.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String str) {
                exerciseName2 = str;
            }
        });

        adminClientProfileViewModel.newExerciseReps2.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String str) {
                exerciseReps2 = str;
            }
        });

        adminClientProfileViewModel.newExerciseName3.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String str) {
                exerciseName3 = str;
            }
        });

        adminClientProfileViewModel.newExerciseReps3.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String str) {
                exerciseReps3 = str;
            }
        });

        adminClientProfileViewModel.newExerciseName4.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String str) {
                exerciseName4 = str;
            }
        });

        adminClientProfileViewModel.newExerciseReps4.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String str) {
                exerciseReps4 = str;
            }
        });

        adminClientProfileViewModel.newExerciseName5.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String str) {
                exerciseName5 = str;
            }
        });

        adminClientProfileViewModel.newExerciseReps5.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String str) {
                exerciseReps5 = str;
            }
        });

        adminClientProfileViewModel.generatedExerciseName.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String str) {
                generatedExerciseName = str;

                //generatedExerciseArray.add(totalExerciseEditTexts).setExerciseName(generatedExerciseName);
            }
        });

        adminClientProfileViewModel.generatedExerciseReps.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String str) {
                generatedExerciseReps = str;
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
        Log.d(TAG, String.valueOf(exerciseNameEditTextArray.size()));

        // add edittext
        EditText et = new EditText(getContext());
        EditText et2 = new EditText(getContext());
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        et.setLayoutParams(p);
        et2.setLayoutParams(p);

        String exStr = "Excercise: ";
        et.setHintTextColor(Color.WHITE);//getResources().getColor(R.color.white));
        et2.setHintTextColor(Color.WHITE);
        et.setHint("Enter exercise");
        et2.setHint("Enter reps");
        et.setId(totalExerciseNameEditTexts + 1);
        et2.setId(totalExerciseRepsEditTexts + 1);

        ll.addView(et);
        ll.addView(et2);

        exerciseNameEditTextArray.add(totalExerciseNameEditTexts, et);
        exerciseRepsEditTextArray.add(totalExerciseRepsEditTexts, et2);

        totalExerciseNameEditTexts++;
        totalExerciseRepsEditTexts++;
    }

public void getEtValues(String workoutTitle, String workoutDay, User currentClient, String exerciseName, String exerciseName2,
                        String exerciseName3, String exerciseName4, String exerciseName5, String exerciseReps,
                        String exerciseReps2, String exerciseReps3, String exerciseReps4, String exerciseReps5, LinearLayout ll) {

        ArrayList<Exercise> exArray = new ArrayList<Exercise>();

        Exercise exercise = new Exercise(exerciseName, exerciseReps);
        Exercise exercise2 = new Exercise(exerciseName2, exerciseReps2);
        Exercise exercise3 = new Exercise(exerciseName3, exerciseReps3);
        Exercise exercise4 = new Exercise(exerciseName4, exerciseReps4);
        Exercise exercise5 = new Exercise(exerciseName5, exerciseReps5);

        exArray.add(0, exercise);
        exArray.add(1, exercise2);
        exArray.add(2, exercise3);
        exArray.add(3, exercise4);
        exArray.add(4, exercise5);

        // set exerciseName and exerciseReps
        for (int i=0; i < exerciseNameEditTextArray.size(); i++) {
            String exName = exerciseNameEditTextArray.get(i).getText().toString();
            String exReps = exerciseRepsEditTextArray.get(i).getText().toString();
            Exercise generatedExercise = new Exercise(exName, exReps);
            exArray.add(generatedExercise);
        }

        adminClientProfileViewModel.writeToWorkouts(currentClient, exArray, workoutDay, workoutTitle);
    }

    public void toDatePicker() {
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new ReportListFragment(currentUser, currentClient)).addToBackStack(null).commit();
    }

    public void toWorkoutList() {
//        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
//                new WorkoutListFragment(currentUser, currentClient)).addToBackStack(null).commit();
    }
}
