package zachg.gsctrainingandnutritiontracker;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import zachg.gsctrainingandnutritiontracker.login.LoginFragment;

// Controls the various fragments

public abstract class SingleFragmentActivity extends AppCompatActivity {
    public static FragmentManager fm;

    protected abstract Fragment createFragment();

    private static final String TAG = "SingleFragmentActivity";

    // Returns ID of layout that the activity will inflate
    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            fm.beginTransaction().add(R.id.fragment_container, new LoginFragment()).commit();
        }
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
//        prePop1();
//        prePop2();

    }

//    just to test Firebase
//    public void prePop1() {
//        // Access a Cloud Firestore instance from your Activity
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        // Create a new user with a first and last name
//        Map<String, Object> user = new HashMap<>();
//        user.put("first", "Buford");
//        user.put("nickname", "Mad Dog");
//        user.put("last", "Tannen");
//        user.put("time", 1885);
//
//        // Add a new document with a generated ID
//        db.collection("users")
//                .add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error adding document", e);
//                    }
//                });
//    }
//
//    public void prePop2() {
//        // Access a Cloud Firestore instance from your Activity
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        // Create a new user with a first, middle, and last name
//        Map<String, Object> user = new HashMap<>();
//        user.put("first", "Marty");
//        user.put("last", "McFly");
//        user.put("time", 1985);
//
//        // Add a new document with a generated ID
//        db.collection("users")
//                .add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error adding document", e);
//                    }
//                });
//    }
}