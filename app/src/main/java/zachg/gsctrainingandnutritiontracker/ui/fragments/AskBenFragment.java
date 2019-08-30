package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import zachg.gsctrainingandnutritiontracker.models.Message;
import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;
import zachg.gsctrainingandnutritiontracker.viewmodels.AskBenViewModel;

public class AskBenFragment extends Fragment {

    private Button bSend;
    private File mPhotoFile;
    private ImageButton mPhotoButton;
    private static final int REQUEST_PHOTO = 2;
    private AskBenViewModel mAskBenViewModel;

    private MutableLiveData<ArrayList<Message>> mMessages;
    private FirestoreRepository mRepo = new FirestoreRepository();;
    private MutableLiveData<Boolean> mIsUpdating = new MutableLiveData<>();

    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public AskBenFragment() {
        // required empty constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancestate) {
        View v = inflater.inflate(R.layout.fragment_ask_ben, container, false);

        mAskBenViewModel = ViewModelProviders.of(getActivity()).get(AskBenViewModel.class);
        mAskBenViewModel.init();

        bSend = v.findViewById(R.id.bSend);
        bSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mAskBenViewModel.sendMessage();
                Toast.makeText(getActivity(), "Message sent!", Toast.LENGTH_SHORT).show();
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

        final EditText etMsgTitle = v.findViewById(R.id.etMsgTitle);
        final EditText etMsgBody = v.findViewById(R.id.etMsgBody);

        return v;
    }
}
