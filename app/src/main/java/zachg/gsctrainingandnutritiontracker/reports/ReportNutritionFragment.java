package zachg.gsctrainingandnutritiontracker.reports;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
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
import static zachg.gsctrainingandnutritiontracker.login.LoginHandler.currentSelectedUser;
import static zachg.gsctrainingandnutritiontracker.login.LoginHandler.currentUser;
import static zachg.gsctrainingandnutritiontracker.login.LoginHandler.isAdmin;

// ReportFragment builds out the fragment that hosts the Report objects

public class ReportNutritionFragment extends Fragment {
    private static final String ARG_REPORT_ID = "report_id";
    private static final int REQUEST_DATE = 0;
    private Button mReportButton;
    private File mPhotoFile;
    private ImageView mPhotoView;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private String mClientName;
    private TextView tvClientName;
    private String mDate;
    private TextView tvDate;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_workout_report, container, false);

        // nutrition info
        // add nutrition totals as input
        if (isAdmin) {
            currentUser.setClientName(currentUser.getFirstName(), currentUser.getLastName());
            mClientName = currentUser.getClientName();
        } else {
            currentSelectedUser.setClientName(currentSelectedUser.getFirstName(), currentSelectedUser.getLastName());
            mClientName = currentSelectedUser.getClientName();
        }
        String mClientNameFormat = getResources().getString(R.string.clientName);
        final String mClientNameMsg = String.format(mClientNameFormat, mClientName);
        tvClientName = v.findViewById(R.id.client_name);
        tvClientName.setText(mClientNameMsg);

        // get date of day clicked, not current date
        mDate = String.valueOf(Calendar.getInstance().getTime());
        String mDateFormat = getResources().getString(R.string.date);
        final String mDateMsg = String.format(mDateFormat, mDate);
        tvDate = v.findViewById(R.id.tvDate);
        tvDate.setText(mDateMsg);

        final EditText etMealItem = v.findViewById(R.id.etMealItem);

        // Take Only Ints
        final EditText etCalories = v.findViewById(R.id.etCalories);
        final EditText etFat = v.findViewById(R.id.etFat);
        final EditText etCarbs = v.findViewById(R.id.etCarbs);
        final EditText etProtein = v.findViewById(R.id.etProtein);

        // Calculate the totals
        final int mTotalCalories = Integer.parseInt(etCalories.getText().toString()); // + all dynamic Calorie fields
        final int mTotalFat = Integer.parseInt(etFat.getText().toString());
        final int mTotalCarbs = Integer.parseInt(etCarbs.getText().toString());
        final int mTotalProtein = Integer.parseInt(etProtein.getText().toString());

        mReportButton = (Button) v.findViewById(R.id.bSendReport);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String mMealItem = etMealItem.getText().toString();
                String mCalories = etCalories.getText().toString();
                String mFat = etFat.getText().toString();
                String mCarbs = etCarbs.getText().toString();
                String mProtein = etProtein.getText().toString();

                // this will increment
                int mExerciseNum = 1;
                // when called, increase by 1

                // Access a Cloud Firestore instance
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                // Create a Report
                Map<String, Object> report = new HashMap<>();
                report.put("client name", mClientNameMsg);
                report.put("date", mDateMsg);
//                report.put("weight", mDailyWeight);
//                report.put("Exercise " + mExerciseNum, mExerciseName);
//                report.put("Weight Used ", mWeightUsed);
//                report.put("# of reps", mRepsEntry);
//                report.put("Workout comments:", mWorkoutComments);
                report.put("Meal", mMealItem);
                report.put("Calories", mCalories);
                report.put("Fat", mFat);
                report.put("Carbs", mCarbs);
                report.put("Protein", mProtein);
//                report.put("Total Calories", mTotalCalories);
//                report.put("Total Fat", mTotalFat);
//                report.put("Total Carbs", mTotalCarbs);
//                report.put("Total Protein", mTotalProtein);


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

        v.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            public void onSwipeRight() {
                // go to WorkoutFragment
                Toast.makeText(getActivity(), "you swiped right", Toast.LENGTH_LONG).show();
            }
            public void onSwipeLeft() {
                // go to next date
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new ReportWorkoutFragment()).addToBackStack(null).commit();
            }
        });

        mPhotoView =(ImageView)v.findViewById(R.id.client_photo);
        updatePhotoView();

        return v;
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
        if (isAdmin) {
            inflater.inflate(R.menu.admin_menu, menu);
        } else {
            inflater.inflate(R.menu.user_menu, menu);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bViewProfile:
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new UserProfileFragment()).addToBackStack(null).commit();
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