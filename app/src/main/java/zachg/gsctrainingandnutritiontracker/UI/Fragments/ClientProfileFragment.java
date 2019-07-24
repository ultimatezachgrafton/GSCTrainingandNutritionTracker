package zachg.gsctrainingandnutritiontracker.UI.Fragments;

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

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.UI.Activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.utils.PictureUtils;

public class ClientProfileFragment extends Fragment implements View.OnClickListener {

    private final int REQUEST_PHOTO = 2;
    private File mPhotoFile;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    TextView tvClientName, tvGender, tvDateJoined, tvBirthDate, tvIsAdmin;
    Button bToDatePicker;
    private final String ARG_USER_ID = "user_id";

    public ClientProfileFragment newInstance(String clientId){
        Bundle args = new Bundle();
        args.putString("id", clientId);
        ClientProfileFragment fragment = new ClientProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_client_profile, container, false);
        bToDatePicker = v.findViewById(R.id.bToDatePicker);

        AdminListFragment af = new AdminListFragment();

        tvClientName = v.findViewById(R.id.tvClientName);
        af.currentSelectedUser.setClientName(af.currentSelectedUser.getFirstName(), af.currentSelectedUser.getLastName());
        tvClientName.setText(af.currentSelectedUser.getClientName());
        tvGender = v.findViewById(R.id.tvGender);
        tvGender.setText(af.currentSelectedUser.getGender());
        tvBirthDate = v.findViewById(R.id.tvBirthDate);
        tvBirthDate.setText(af.currentSelectedUser.getBirthdate());
        tvDateJoined = v.findViewById(R.id.tvDateJoined);
        tvDateJoined.setText(af.currentSelectedUser.getDateJoined());
        tvIsAdmin = v.findViewById(R.id.tvIsAdmin);
        if (af.currentSelectedUser.getIsAdmin()) {
            tvIsAdmin.setText(R.string.isAdminUser);
        } else {
            tvIsAdmin.setText(R.string.isAdminAdmin);
        }

        // progress chart

        // to client's datepicker
        bToDatePicker.setOnClickListener(this);

        mPhotoButton = v.findViewById(R.id.bCamera);
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

        mPhotoView = v.findViewById(R.id.profile_photo);

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
