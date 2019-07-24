package zachg.gsctrainingandnutritiontracker.UI.Fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.UI.Activities.SingleFragmentActivity;

import static android.content.ContentValues.TAG;

public class AskBenFragment extends Fragment implements View.OnClickListener {

    private Button bSend;
    private File mPhotoFile;
    private ImageButton mPhotoButton;
    private static final int REQUEST_PHOTO = 2;

    public AskBenFragment() {
        // required empty constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancestate) {
        View v = inflater.inflate(R.layout.fragment_ask_ben, container, false);

        bSend = v.findViewById(R.id.bSend);

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

        bSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send message to Ben's message inbox
                String msgTitle = etMsgTitle.getText().toString();
                String msgBody = etMsgBody.getText().toString();
                // String clientName = clientName
                // String msgDate = get date
                // get photo file

                FirebaseFirestore msgDb = FirebaseFirestore.getInstance();

                // Create a new user
                Map<String, Object> msg = new HashMap<>();
                msg.put("msgTitle", msgTitle);
                msg.put("msgBody", msgBody);
                //msg.put("msgDate", msgDate);
                //msg.put("clientName", clientName);

                // Add user as a new document with a generated ID
                msgDb.collection("messages")
                        .add(msg)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error adding document", e);
                                            }
                                        });

                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                      new DatePickerFragment()).addToBackStack(null).commit();

             }
        });
        return v;
    }

    @Override
    public void onClick(View v) {

    }
}
