package zachg.gsctrainingandnutritiontracker.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.adapters.ExerciseForViewOnlyListAdapter;
import zachg.gsctrainingandnutritiontracker.adapters.ExerciseListAdapter;
import zachg.gsctrainingandnutritiontracker.adapters.WorkoutListAdapter;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentReportBinding;
import zachg.gsctrainingandnutritiontracker.models.Exercise;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.utils.PictureUtils;
import zachg.gsctrainingandnutritiontracker.viewmodels.ClientReportViewModel;

public class ClientReportFragment extends Fragment {

    // For Users to fill out their workout as they complete it

    private ClientReportViewModel clientReportViewModel = new ClientReportViewModel();
    private ExerciseForViewOnlyListAdapter exerciseListAdapter;

    private FragmentReportBinding binding;

    private File photoFile;
    private ImageView photoView;
    private String clientName, dateString;
    private TextView tvClientName, tvDate;
    private ArrayList<Workout> workouts = new ArrayList<>();
    private Workout workout = new Workout();
    private ImageView profilePhoto;

    private Date date;
    private User user = new User();
    private User client = new User();
    private Report report = new Report();

    private static final int REQUEST_CONTACT = 1;
    public String TAG = "ClientReportFragment";

    public ClientReportFragment() {}

    public ClientReportFragment(User user, User client, Workout workout, Report report) {
        this.workout = workout;
        this.user = user;
        this.client = client;
        this.report = report;
        this.dateString = report.getDateString();
        report.setClientName(user.getClientName());
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentReportBinding.inflate(inflater, container, false);
        final View v = binding.getRoot();

        binding.setReport(report);
        binding.setUser(user);
        binding.setClient(client);
        binding.setFragment(this);

        binding.setModel(clientReportViewModel);
        clientReportViewModel = ViewModelProviders.of(getActivity()).get(ClientReportViewModel.class);
        binding.setLifecycleOwner(this);
        clientReportViewModel.init(user, report, workout);

        clientReportViewModel.getExerciseLiveData().observe(this, new Observer<FirestoreRecyclerOptions<Exercise>>() {
            @Override
            public void onChanged(FirestoreRecyclerOptions<Exercise> e) {
                initRecyclerView(e);
                exerciseListAdapter.startListening();
            }
        });

        clientReportViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (!aBoolean) {
                    binding.rvExercise.smoothScrollToPosition(clientReportViewModel.getExerciseLiveData().getValue().getSnapshots().size() - 1);
                }
            }
        });

        clientReportViewModel.getOnError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

        clientReportViewModel.getOnSuccess().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                goToClientPortal();
            }
        });

        photoFile = getPhotoFile(client);
        profilePhoto = v.findViewById(R.id.client_photo);
        updatePhotoView();

        return v;
    }

    private void initRecyclerView(FirestoreRecyclerOptions<Exercise> exercises) {
        exerciseListAdapter = new ExerciseForViewOnlyListAdapter(exercises);
        binding.rvExercise.setAdapter(exerciseListAdapter);
        binding.rvExercise.setLayoutManager(new LinearLayoutManager(getContext()));
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



    private void clearBackStack() {
        if (SingleFragmentActivity.fm.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = SingleFragmentActivity.fm.getBackStackEntryAt(0);
            SingleFragmentActivity.fm.popBackStackImmediate(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public void onSendReportClicked() {
        clientReportViewModel.getExercisesForArray();
    }

    private void goToClientPortal() {
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new ClientPortalFragment(user, client)).addToBackStack(null).commit();
    }
}