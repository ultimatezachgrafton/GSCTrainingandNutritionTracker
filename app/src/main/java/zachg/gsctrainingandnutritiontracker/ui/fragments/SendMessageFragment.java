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
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.io.File;
import java.util.List;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentSendMessageBinding;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.viewmodels.SendMessageViewModel;

public class SendMessageFragment extends Fragment {

    private FragmentSendMessageBinding binding;

    private Button bSend;
    private File photoFile;
    private ImageButton bPhotoButton;
    private User currentUser = new User();
    private static final int REQUEST_PHOTO = 2;
    private SendMessageViewModel sendMessageViewModel;

    public SendMessageFragment(User user) {
        this.currentUser = user;
    }

    public SendMessageFragment() {
        // required empty constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancestate) {
        binding = FragmentSendMessageBinding.inflate(inflater, container, false);
        View v = binding.getRoot();
        sendMessageViewModel = ViewModelProviders.of(getActivity()).get(SendMessageViewModel.class);
        sendMessageViewModel.init();

        bSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendMessageViewModel.sendMessage();
                Toast.makeText(getActivity(), "Message sent!", Toast.LENGTH_SHORT).show();
            }
        });

        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = photoFile != null;
        bPhotoButton.setEnabled(canTakePhoto);

        bPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "zachg.bensfitnessapp.fileprovider", photoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                List<ResolveInfo> cameraActivities = getActivity().getPackageManager().
                        queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        return v;
    }
}
