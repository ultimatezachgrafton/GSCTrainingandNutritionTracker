package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.io.File;
import java.util.List;

import javax.annotation.Nullable;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentClientProfileBinding;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.utils.PictureUtils;
import zachg.gsctrainingandnutritiontracker.viewmodels.ClientProfileViewModel;

public class ClientProfileFragment extends Fragment implements View.OnClickListener {

    FragmentClientProfileBinding binding;

    private ClientProfileViewModel clientProfileViewModel;
    private final int REQUEST_PHOTO = 2;
    private Button bToDatePicker;
    private File photoFile;
    private ImageView photoView;
    private ImageButton bCamera;
    private final String ARG_USER_ID = "user_id";

    private User currentUser = new User();

    public ClientProfileFragment() {}

    public ClientProfileFragment(User user) {
        clientProfileViewModel = new ClientProfileViewModel(user);
        this.currentUser = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        binding = FragmentClientProfileBinding.inflate(inflater, container, false);
        final View v = binding.getRoot();
        binding.setUser(currentUser);

//        bToDatePicker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
//                        new CalendarFragment(currentUser)).addToBackStack(null).commit();
//            }
//        });

        clientProfileViewModel = ViewModelProviders.of(getActivity()).get(ClientProfileViewModel.class);
        photoFile = getPhotoFile(currentUser);
        updatePhotoView();
//        bCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                takePhoto();
//            }
//        });

        return v;
    }


    public File getPhotoFile(User user) {
        File filesDir = getActivity().getFilesDir();
        return new File( filesDir, user.getPhotoFilename());
    }

    public void takePhoto() {
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imagePath = new File(getContext().getFilesDir(), "images");
        photoFile = new File(imagePath, "default_image.jpg");
        Uri uri = FileProvider.getUriForFile(getActivity(),
                "zachg.gsctrainingandnutritiontracker.fileprovider", photoFile);
        captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        List<ResolveInfo> cameraActivities = getActivity().getPackageManager().
                queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo activity : cameraActivities) {
            getActivity().grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }

        startActivityForResult(captureImage, REQUEST_PHOTO);
    }

    private void updatePhotoView() {
        if (photoFile == null || !photoFile.exists()) {
//            photoView.setImageDrawable(null);
//            photoView.setContentDescription(
//                    getString(R.string.report_photo_no_image_description)
//            );
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(photoFile.getPath(), getActivity());
            photoView.setImageBitmap(bitmap);
            photoView.setContentDescription(
                    getString(R.string.report_photo_image_description)
            );
        }
    }

    @Override
    public void onClick(View v) {

    }
}