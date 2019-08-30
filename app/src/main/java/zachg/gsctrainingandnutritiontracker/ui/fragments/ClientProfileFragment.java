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
import android.widget.ImageView;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.io.File;
import java.util.List;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentClientProfileBinding;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.utils.PictureUtils;
import zachg.gsctrainingandnutritiontracker.viewmodels.ClientProfileViewModel;


public class ClientProfileFragment extends Fragment implements View.OnClickListener {

    FragmentClientProfileBinding mBinding;

    private ClientProfileViewModel mClientProfileViewModel;
    private final int REQUEST_PHOTO = 2;
    private Button bToDatePicker;
    private File mPhotoFile;
    private ImageView mPhotoView;
    private final String ARG_USER_ID = "user_id";

    private User mCurrentUser;

    public ClientProfileFragment(User currentUser) {
        mClientProfileViewModel = new ClientProfileViewModel(currentUser);
        this.mCurrentUser = currentUser;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        mBinding = FragmentClientProfileBinding.inflate(inflater, container, false);
        final View v = mBinding.getRoot();

        mBinding.setUser(mCurrentUser);
        bToDatePicker = v.findViewById(R.id.bToDatePicker);
        bToDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new DatePickerFragment(mCurrentUser)).addToBackStack(null).commit();
            }
        });

        mClientProfileViewModel = ViewModelProviders.of(getActivity()).get(ClientProfileViewModel.class);
        mPhotoView = v.findViewById(R.id.profile_photo);
        updatePhotoView();

        return v;
    }

    public void takePhoto() {
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
    public void onClick(View v) {

    }
}