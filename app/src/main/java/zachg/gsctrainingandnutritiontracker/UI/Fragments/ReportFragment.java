package zachg.gsctrainingandnutritiontracker.UI.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
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

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.Calendar;

import zachg.gsctrainingandnutritiontracker.Models.User;
import zachg.gsctrainingandnutritiontracker.Models.Workout;
import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.Repositories.FirestoreRepository;
import zachg.gsctrainingandnutritiontracker.UI.Activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.UI.Adapters.WorkoutListAdapter;
import zachg.gsctrainingandnutritiontracker.ViewModels.ReportViewModel;
import zachg.gsctrainingandnutritiontracker.utils.OnSwipeTouchListener;
import zachg.gsctrainingandnutritiontracker.utils.PictureUtils;

import static zachg.gsctrainingandnutritiontracker.UI.Fragments.AdminListFragment.currentSelectedUser;
import static zachg.gsctrainingandnutritiontracker.UI.Fragments.DatePickerFragment.currentSelectedReport;

// ReportFragment builds out the fragment that hosts the Report objects

public class ReportFragment extends Fragment {
    private ReportViewModel mReportViewModel;
    private RecyclerView mReportRecyclerView;
    private WorkoutListAdapter mWorkoutListAdapter;
    public FirestoreRepository mRepo = new FirestoreRepository();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private Button bReport;
    private File mPhotoFile;
    private ImageView mPhotoView;
    private String mClientName;
    private TextView tvClientName;
    private String mDate;
    private TextView tvDate;
    private Button bAddUpdate; // temp

    public ReportFragment() {}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_workout_report, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        mReportViewModel = ViewModelProviders.of(getActivity()).get(ReportViewModel.class);
        mReportViewModel.init();
        initRecyclerView(v);

        // TODO: STOPGAP method of by-passing the issue of currentSelectedUser != admin - FIX
        if (currentSelectedUser.getIsAdmin()) {
            User currentUser = new User();
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

        if (currentSelectedReport.getIsNew()) {
            mDate = String.valueOf(Calendar.getInstance().getTime());
            String mDateFormat = getResources().getString(R.string.date);
            final String mDateMsg = String.format(mDateFormat, mDate);
            tvDate = v.findViewById(R.id.tvDate);
            tvDate.setText(mDateMsg);
        }

        final EditText etWeight = v.findViewById(R.id.etWeight);

        bAddUpdate = (Button) v.findViewById(R.id.bAddUpdate);

        bReport = v.findViewById(R.id.bSendReport);
        if (currentSelectedReport.getIsNew()) {
            bReport.setText(R.string.send); // sets Send Report button
        } else {
            bReport.setText(R.string.update); // sets Update Report button
        }

        bAddUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mReportViewModel.writeReport();
            }
        });

        v.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            public void onSwipeLeft() {
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new ReportFragment()).addToBackStack(null).commit();
            }
            public void onSwipeRight() {
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new ReportFragment()).addToBackStack(null).commit();
            }
        });

        mPhotoView =(ImageView)v.findViewById(R.id.client_photo);
        updatePhotoView();

        return v;
    }

    private void initRecyclerView(View v) {
        FirestoreRecyclerOptions<Workout> options = mRepo.setWorkouts();
        mWorkoutListAdapter = new WorkoutListAdapter(options);
        mReportRecyclerView = v.findViewById(R.id.rvWorkout);
        mReportRecyclerView.setHasFixedSize(true);
        mReportRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mReportRecyclerView.setAdapter(mWorkoutListAdapter);
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

        if (mWorkoutListAdapter != null) {
            mWorkoutListAdapter.stopListening();
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