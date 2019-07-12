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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import zachg.gsctrainingandnutritiontracker.ClientProfileFragment;
import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.inbox.AskBenFragment;
import zachg.gsctrainingandnutritiontracker.inbox.InboxFragment;
import zachg.gsctrainingandnutritiontracker.login.LoginFragment;
import zachg.gsctrainingandnutritiontracker.login.RegisterFragment;
import zachg.gsctrainingandnutritiontracker.utils.OnSwipeTouchListener;
import zachg.gsctrainingandnutritiontracker.utils.PictureUtils;

import static android.content.ContentValues.TAG;
import static zachg.gsctrainingandnutritiontracker.AdminList.AdminListFragment.currentSelectedUser;
import static zachg.gsctrainingandnutritiontracker.login.LoginHandler.currentUser;
import static zachg.gsctrainingandnutritiontracker.login.LoginHandler.isAdmin;

// ReportFragment builds out the fragment that hosts the Report objects

public class ReportWorkoutFragment extends Fragment {
    private RecyclerView mWorkoutRecyclerView;
    private ReportListAdapter adapter;
    private Button mReportButton;
    private File mPhotoFile;
    private ImageView mPhotoView;
    private boolean isNew;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private String mClientName;
    private TextView tvClientName;
    private String mDate;
    private TextView tvDate;

    private static ArrayList<Report> mReports = new ArrayList<>();

    public ReportWorkoutFragment() {}

    static {
        FirebaseFirestore.setLoggingEnabled(true);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_workout_report, container, false);

        if (isAdmin) {
            currentUser.setClientName(currentUser.getFirstName(), currentUser.getLastName());
            mClientName = currentUser.getClientName();
        } else {
            currentSelectedUser.setClientName(currentSelectedUser.getFirstName(), currentSelectedUser.getLastName());
            mClientName = currentSelectedUser.getClientName();
        }

        ReportHandler.fetchReportsByUserDate(mReports, currentSelectedUser.getClientName());

        adapter = new ReportListAdapter(ReportHandler.getReportOptions(ReportHandler.reportColRef));

        mWorkoutRecyclerView = v.findViewById(R.id.rvWorkout);
        mWorkoutRecyclerView.setHasFixedSize(true);
        mWorkoutRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mWorkoutRecyclerView.setAdapter(adapter);

        String mClientNameFormat = getResources().getString(R.string.clientName);
        final String mClientNameMsg = String.format(mClientNameFormat, mClientName);
        tvClientName = v.findViewById(R.id.client_name);
        tvClientName.setText(mClientNameMsg);

        // get original date, not current date if updated
        mDate = String.valueOf(Calendar.getInstance().getTime());
        String mDateFormat = getResources().getString(R.string.date);
        final String mDateMsg = String.format(mDateFormat, mDate);
        tvDate = v.findViewById(R.id.tvDate);
        tvDate.setText(mDateMsg);

        final TextView tvExerciseName = v.findViewById(R.id.tvExerciseName);

        final EditText etWeight = v.findViewById(R.id.etWeight);

        // once entries are input, they are saved locally

        mReportButton = (Button) v.findViewById(R.id.bSendReport);

        // Send Report to database
        mReportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String mDailyWeight = etWeight.getText().toString();
//                String mExerciseName = etExerciseName.getText().toString();
//                String mWeightUsed = etWeightUsedEntry.getText().toString();
//                String mRepsEntry = etRepsEntry.getText().toString();
//                String mWorkoutComments = etWorkoutComments.getText().toString();

                // Access a Cloud Firestore instance
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                // Create a Report
                Map<String, Object> report = new HashMap<>();
                report.put("client name", mClientNameMsg);
                report.put("date", mDateMsg);
                report.put("weight", mDailyWeight);
//                report.put("Exercise ", mExerciseName);
//                report.put("Weight Used ", mWeightUsed);
//                report.put("# of reps", mRepsEntry);
//                report.put("Workout comments:", mWorkoutComments);

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
            // "Send Report" turns into "Update Report"
        });

        v.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            public void onSwipeRight() {
                // go to NutritionFragment
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new ReportNutritionFragment()).addToBackStack(null).commit();
            }
            public void onSwipeLeft() {
                // go to prev date
                Toast.makeText(getActivity(), "ya swiped left", Toast.LENGTH_LONG).show();
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
        if (currentSelectedUser.getIsAdmin()) {
            inflater.inflate(R.menu.admin_menu, menu);
        } else {
            inflater.inflate(R.menu.user_menu, menu);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bViewProfile:
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new ClientProfileFragment()).addToBackStack(null).commit();
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