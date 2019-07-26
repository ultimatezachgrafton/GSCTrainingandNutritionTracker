package zachg.gsctrainingandnutritiontracker.UI.Fragments;

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
import java.util.HashMap;
import java.util.Map;

import zachg.gsctrainingandnutritiontracker.Models.Workout;
import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.UI.Activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.UI.Adapters.ReportListAdapter;
import zachg.gsctrainingandnutritiontracker.ViewModels.LoginHandler;
import zachg.gsctrainingandnutritiontracker.ViewModels.ReportHandler;
import zachg.gsctrainingandnutritiontracker.utils.OnSwipeTouchListener;
import zachg.gsctrainingandnutritiontracker.utils.PictureUtils;

// ReportFragment builds out the fragment that hosts the Report objects

public class ReportWorkoutFragment extends Fragment {
    private RecyclerView mWorkoutRecyclerView;
    private ReportListAdapter adapter;
    private Button mReportButton;
    private File mPhotoFile;
    private ImageView mPhotoView;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private String mClientName;
    private TextView tvClientName;
    private String mDate;
    private TextView tvDate;
    private Button bAddUpdate;
    private Button bNut;

    public ArrayList<Workout> mWorkouts = new ArrayList<>();
    public Workout currentSelectedWorkout = new Workout();

    private LoginHandler lh = new LoginHandler();
    private AdminListFragment af = new AdminListFragment();
    private ReportHandler rh = new ReportHandler();

    public ReportWorkoutFragment() {}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_workout_report, container, false);

        if (lh.isAdmin) {
            lh.currentUser.setClientName(lh.currentUser.getFirstName(), lh.currentUser.getLastName());
            mClientName = lh.currentUser.getClientName();
        } else {
            af.currentSelectedUser.setClientName(af.currentSelectedUser.getFirstName(), af.currentSelectedUser.getLastName());
            mClientName =af.currentSelectedUser.getClientName();
        }

        mWorkouts = rh.getWorkouts(mWorkouts);

        // this is wrong - I think it is listing the Reports for display in RV
        adapter = new ReportListAdapter(rh.getReportOptions(rh.reportQuery));

        mWorkoutRecyclerView = v.findViewById(R.id.rvWorkout);
        mWorkoutRecyclerView.setHasFixedSize(true);
        mWorkoutRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mWorkoutRecyclerView.setAdapter(adapter);

        String mClientNameFormat = getResources().getString(R.string.clientName);
        final String mClientNameMsg = String.format(mClientNameFormat, mClientName);
        tvClientName = v.findViewById(R.id.client_name);
        tvClientName.setText(mClientNameMsg);

        // TODO: get original date, not current date if updated
        mDate = String.valueOf(Calendar.getInstance().getTime());
        String mDateFormat = getResources().getString(R.string.date);
        final String mDateMsg = String.format(mDateFormat, mDate);
        tvDate = v.findViewById(R.id.tvDate);
        tvDate.setText(mDateMsg);

        final EditText etWeight = v.findViewById(R.id.etWeight);

        // once Weight entries are input, they are saved locally

        // temp button
        bNut = v.findViewById(R.id.bNut);
        bAddUpdate = (Button) v.findViewById(R.id.bAddUpdate);

        // TODO: only creates one Report per User per day
        // Send Report to database
        bAddUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                rh.writeReports();
//            }
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
                                    Log.d("mReports", "DocumentSnapshot added with ID: " + documentReference.getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("mReports", "Error adding document", e);
                                }
                            });
            }
            // "Send Report" turns into "Update Report"
        });

        bNut.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new ReportNutritionFragment()).addToBackStack(null).commit();
            }
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
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (af.currentSelectedUser.getIsAdmin()) {
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