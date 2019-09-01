package zachg.gsctrainingandnutritiontracker.ui.fragments;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.ui.adapters.WorkoutListAdapter;
import zachg.gsctrainingandnutritiontracker.utils.OnSwipeTouchListener;
import zachg.gsctrainingandnutritiontracker.utils.PictureUtils;
import zachg.gsctrainingandnutritiontracker.viewmodels.ReportViewModel;

public class ReportFragment extends Fragment {
    private ReportViewModel mReportViewModel = new ReportViewModel();
    private RecyclerView mReportRecyclerView;
    private WorkoutListAdapter mWorkoutListAdapter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    //FragmentReportBinding mBinding;

    private Button bReport;
    private File mPhotoFile;
    private ImageView mPhotoView;
    private String mClientName;
    private TextView tvClientName;
    private String mDateString;
    private TextView tvDate;
    private Button bAddUpdate; // TODO: temp

    private Report mCurrentReport = new Report();
    private Date mDate;
    private User mCurrentUser = new User();

    public ReportFragment() {}

    public ReportFragment(Report report, User user) {
        this.mCurrentReport = report;
        this.mCurrentUser = user;
        this.mCurrentReport.setClientName(user.getClientName());
        this.mClientName = mCurrentReport.getClientName();
        this.mDateString = report.getDateString();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //mBinding = FragmentReportBinding.inflate(inflater, container, false);
        final View v = inflater.inflate(R.layout.fragment_report, container, false);
        //final View v = mBinding.getRoot();
        //mBinding.setReport(mCurrentReport);

        Log.d("mReports", "pre-init: name: " + mCurrentUser.getClientName());
        Log.d("mReports", "pre-init: workout num: " + mCurrentUser.getWorkoutNum());

        mReportViewModel = ViewModelProviders.of(getActivity()).get(ReportViewModel.class);
        mReportViewModel.init(mCurrentUser);

        FirestoreRepository mRepo = new FirestoreRepository();
        FirestoreRecyclerOptions<Workout> workoutOptions = mRepo.getWorkoutsFromRepo(mCurrentUser);
        initRecyclerView(v, workoutOptions);

        mDateString = String.valueOf(Calendar.getInstance().getTime());
        String mDateFormat = getResources().getString(R.string.date);
        final String mDateMsg = String.format(mDateFormat, mDate);
        tvDate = v.findViewById(R.id.tvDate);
        tvDate.setText(mDateMsg);

        bAddUpdate = (Button) v.findViewById(R.id.bAddUpdate);

        bReport = v.findViewById(R.id.bSendReport);
        if (mCurrentReport.getIsNew()) {
            bReport.setText(R.string.send); // sets Send Report button
        } else {
            bReport.setText(R.string.update); // sets Update Report button
        }

        bAddUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("mReports", "click");
                mReportViewModel.writeReport(mCurrentReport, mCurrentUser);
            }
        });

        v.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            public void onSwipeLeft() {
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new ReportFragment(mCurrentReport, mCurrentUser)).addToBackStack(null).commit();
            }
            public void onSwipeRight() {
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new ReportFragment(mCurrentReport, mCurrentUser)).addToBackStack(null).commit();
            }
        });

        mPhotoView = v.findViewById(R.id.client_photo);
        updatePhotoView();

        return v;
    }

//    private void setupBindings(Bundle savedInstanceState) {
//        mBinding = DataBindingUtil.setContentView(getActivity(), R.layout.fragment_report);
//        mReportViewModel = ViewModelProviders.of(this).get(ReportViewModel.class);
//        if (savedInstanceState == null) {
//            mReportViewModel.init();
//        }
//        mBinding.setReport(mCurrentReport);
//    }

    private void initRecyclerView(View v, final FirestoreRecyclerOptions<Workout> workouts) {
        mWorkoutListAdapter = new WorkoutListAdapter(workouts);

        mReportRecyclerView = v.findViewById(R.id.rvWorkout);
        mReportRecyclerView.setHasFixedSize(true);
        mReportRecyclerView.setAdapter(mWorkoutListAdapter);
        mReportRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        mWorkoutListAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mWorkoutListAdapter.stopListening();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (mCurrentUser.getIsAdmin()) {
            inflater.inflate(R.menu.admin_menu, menu);
        } else {
            inflater.inflate(R.menu.user_menu, menu);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bViewProfile:
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new ClientProfileFragment(mCurrentUser)).addToBackStack(null).commit();
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