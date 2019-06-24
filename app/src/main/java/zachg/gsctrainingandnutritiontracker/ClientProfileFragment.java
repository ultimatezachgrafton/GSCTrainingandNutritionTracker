package zachg.gsctrainingandnutritiontracker;

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
import android.widget.TextView;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.util.List;

import zachg.gsctrainingandnutritiontracker.utils.PictureUtils;

import static zachg.gsctrainingandnutritiontracker.login.LoginHandler.currentSelectedUser;

public class ClientProfileFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_PHOTO = 2;
    private File mPhotoFile;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    TextView tvClientName, tvGender, tvDateJoined, tvBirthDate, tvIsAdmin;
    Button bToDatePicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_client_profile, container, false);
        bToDatePicker = v.findViewById(R.id.bToDatePicker);

        tvClientName = v.findViewById(R.id.tvClientName);
        tvClientName.setText(currentSelectedUser.getClientName());
        tvGender = v.findViewById(R.id.tvGender);
        tvGender.setText(currentSelectedUser.getGender());
        tvBirthDate = v.findViewById(R.id.tvBirthDate);
        tvBirthDate.setText(currentSelectedUser.getBirthday());
        tvDateJoined = v.findViewById(R.id.tvDateJoined);
        tvDateJoined.setText(currentSelectedUser.getDateJoined());
        tvIsAdmin = v.findViewById(R.id.tvIsAdmin);
        if (currentSelectedUser.getIsAdmin()) {
            tvIsAdmin.setText(R.string.isAdminUser);
        } else {
            tvIsAdmin.setText(R.string.isAdminAdmin);
        }

        // progress chart

        // to client's datepicker
        bToDatePicker.setOnClickListener(this);


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
    public void onClick(View v) {
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container, new DatePickerFragment()).addToBackStack(null).commit();
    }
}
