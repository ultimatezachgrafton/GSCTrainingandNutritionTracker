package zachg.gsctrainingandnutritiontracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import zachg.gsctrainingandnutritiontracker.utils.PictureUtils;

// ReportFragment builds out the fragment that hosts the Report objects

public class ReportFragment extends Fragment {
    private static final String ARG_REPORT_ID = "report_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT = 1;
    private static final int REQUEST_PHOTO = 2;
    private Report mReport;
    private File mPhotoFile;
    private Button mDateButton;
    private Button mClientButton;
    private Button mReportButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private Callbacks mCallbacks;

    // Required interface for hosting activities
    public interface Callbacks {
        void onReportUpdated(Report mReport);
    }

    public static ReportFragment newInstance(UUID reportId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_REPORT_ID, reportId);

        ReportFragment fragment = new ReportFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Fetches a Report from arguments by fetching intent to pull UUID into variable
        UUID reportId = (UUID) getArguments().getSerializable(ARG_REPORT_ID);
        //mReport = ReportLab.get(getActivity()).getReport(reportId);
        //mPhotoFile = ReportLab.get(getActivity()).getPhotoFile(mReport);
        //mReport = new Report();
    }

    @Override
    public void onPause() {
        super.onPause();
        //ReportLab.get(getActivity()).updateReport(mReport);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_report, container, false);
        /*updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mReport.getDate());
                dialog.setTargetFragment(ReportFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        }); */

        mReportButton = (Button) v.findViewById(R.id.report_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getDate());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.report_report_subject));
                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);

        //This is for Ben Only Mode
        /*
        mClientButton = v.findViewById(R.id.add_new_client);
        mClientButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });
        if (mReport.getClientName() != null) {
            mReportButton.setText(mReport.getClientName());
        }*/

        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact,
                PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mClientButton.setEnabled(false);
        }

        mPhotoButton = (ImageButton) v.findViewById(R.id.report_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
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

        mPhotoView = (ImageView) v.findViewById(R.id.client_photo);
        updatePhotoView();

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mReport.setDate(date);
            updateReport();
            updateDate();
        } else if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            // Specify which fields query should return values for
            String[] queryFields = new String[] {
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            // Perform query - the ContactUri is the "where" clause here
            Cursor c = getActivity().getContentResolver().query(contactUri,
                    queryFields, null, null, null);
            try {
                // Double-check results
                if (c.getCount() == 0) {
                    return;
                }
                // Pull out first column first row of data for suspect's name
                c.moveToFirst();
                String client = c.getString(0);
                mReport.setClientName(client);
                updateReport();
                mClientButton.setText(client);
            } finally {
                c.close();
            }
        } else if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "zachg.bensfitnessapp.fileprovider",
                    mPhotoFile);
            getActivity().revokeUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updateReport();
            updatePhotoView();
        }
    }

    private void updateReport() {
        //ReportLab.get(getActivity()).updateReport(mReport);
        mCallbacks.onReportUpdated(mReport);
    }

    private void updateDate() {
        mDateButton.setText(mReport.getDate().toString());
    }

    // Creates four strings and pieces them together to return a complete report
    private String getDate() {
        String dateFormat = "EEE, MMM ddd";
        String dateString = DateFormat.format(dateFormat, mReport.getDate()).toString();

        return dateString;
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
}

/*
private void displayUserDetails() {
        User user = userLocalStore.getLoggedInUser();
        etUsername.setText(user.username);
        etClientName.setText(user.clientName);
        etBirthday.setText(user.birthday);
        etGender.setText(user.gender);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (authenticate() == true) {
            displayUserDetails();
        }
    }

    private boolean authenticate() {
        return userLocalStore.getUserLoggedIn();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bLogout:

                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }
 */
