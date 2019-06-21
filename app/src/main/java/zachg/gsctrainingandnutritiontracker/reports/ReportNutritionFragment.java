package zachg.gsctrainingandnutritiontracker.reports;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.inbox.AskBenFragment;
import zachg.gsctrainingandnutritiontracker.inbox.InboxFragment;
import zachg.gsctrainingandnutritiontracker.login.LoginFragment;
import zachg.gsctrainingandnutritiontracker.login.RegisterFragment;
import zachg.gsctrainingandnutritiontracker.utils.OnSwipeTouchListener;
import zachg.gsctrainingandnutritiontracker.utils.PictureUtils;

import static android.content.ContentValues.TAG;
import static zachg.gsctrainingandnutritiontracker.login.LoginHandler.currentUser;

// ReportFragment builds out the fragment that hosts the Report objects

public class ReportNutritionFragment extends Fragment {
    private static final String ARG_REPORT_ID = "report_id";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT = 1;
    private static final int REQUEST_PHOTO = 2;
    private File mPhotoFile;
    private Button mReportButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private String mClientName;
    private TextView tvClientName;
    private String mDate;
    private TextView tvDate;
    private String mGender;
    private TextView tvGender;
    private String mBirthDate;
    private TextView tvBirthDate;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_workout_report, container, false);

        currentUser.setClientName(currentUser.getFirstName(), currentUser.getLastName());
        mClientName = currentUser.getClientName();
        String mClientNameFormat = getResources().getString(R.string.clientName);
        final String mClientNameMsg = String.format(mClientNameFormat, mClientName);
        tvClientName = v.findViewById(R.id.client_name);
        tvClientName.setText(mClientNameMsg);

        // get date of day clicked, not current date
        mDate = String.valueOf(Calendar.getInstance().getTime());
        String mDateFormat = getResources().getString(R.string.date);
        final String mDateMsg = String.format(mDateFormat, mDate);
        tvDate = v.findViewById(R.id.date);
        tvDate.setText(mDateMsg);

        mGender = currentUser.getGender();
        String mGenderFormat = getResources().getString(R.string.gender);
        String mGenderMsg = String.format(mGenderFormat, mGender);
        tvGender = v.findViewById(R.id.gender);
        tvGender.setText(mGenderMsg);

        mBirthDate = currentUser.getBirthday();
        String mBirthDateFormat = getResources().getString(R.string.birthday);
        String mBirthDateMsg = String.format(mBirthDateFormat, mBirthDate);
        tvBirthDate = v.findViewById(R.id.birthday);
        tvBirthDate.setText(mBirthDateMsg);

        final EditText etWeight = v.findViewById(R.id.etWeight);
        final EditText etExerciseName = v.findViewById(R.id.etExerciseName);
        final EditText etRepsEntry = v.findViewById(R.id.etRepsEntry);
        final EditText etWeightUsedEntry = v.findViewById(R.id.etWeightUsedEntry);
        final EditText etWorkoutComments = v.findViewById(R.id.etWorkoutComments);

        mReportButton = (Button) v.findViewById(R.id.bSendReport);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String mDailyWeight = etWeight.getText().toString();
                String mExerciseName = etExerciseName.getText().toString();
                String mWeightUsed = etWeightUsedEntry.getText().toString();
                String mRepsEntry = etRepsEntry.getText().toString();
                String mWorkoutComments = etWorkoutComments.getText().toString();

                // this will increment
                int mExerciseNum = 1;
                // when called, increase by 1

                // Access a Cloud Firestore instance
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                // Create a Report
                Map<String, Object> report = new HashMap<>();
                report.put("client name", mClientNameMsg);
                report.put("date", mDateMsg);
                report.put("weight", mDailyWeight);
                report.put("Exercise " + mExerciseNum, mExerciseName);
                report.put("Weight Used ", mWeightUsed);
                report.put("# of reps", mRepsEntry);
                report.put("Workout comments:", mWorkoutComments);

                // Add user as a new document with a generated ID
                db.collection("reports")
                        .add(report)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
            }
        });

        mPhotoButton = (ImageButton) v.findViewById(R.id.report_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null;
        mPhotoButton.setEnabled(canTakePhoto);

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "zachg.bensfitnessapp.fileprovider", mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                List<ResolveInfo> cameraActivities = getActivity().getPackageManager().
                        queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView) v.findViewById(R.id.client_photo);
        updatePhotoView();

        v.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            public void onSwipeRight() {
                // go to WorkoutFragment or prev date
                Toast.makeText(getActivity(), "hi", Toast.LENGTH_LONG).show();
            }
            public void onSwipeLeft() {
                // go to NutritionFragment, or next date
                Toast.makeText(getActivity(), "hi", Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
            mPhotoView.setContentDescription(
                    getString(R.string.report_photo_no_image_description)
            );
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
            mPhotoView.setContentDescription(
                    getString(R.string.report_photo_image_description)
            );
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (currentUser.getIsAdmin()) {
            inflater.inflate(R.menu.admin_menu, menu);
        } else {
            inflater.inflate(R.menu.user_menu, menu);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bAskBen:
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new AskBenFragment()).addToBackStack(null).commit();
                return true;
            case R.id.bAddNewClient:
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new RegisterFragment()).addToBackStack(null).commit();
                return true;
            case R.id.bInbox:
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new InboxFragment()).addToBackStack(null).commit();
                return true;
            case R.id.bLogout:
                mAuth.signOut();
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new LoginFragment()).addToBackStack(null).commit();
                Toast.makeText(getActivity(), "Logged out", Toast.LENGTH_SHORT).show();
                return true;
        } return super.onOptionsItemSelected(item);
    }
}