package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.ui.fragments.CalendarFragment;

import static android.content.ContentValues.TAG;

public class RegisterViewModel extends ViewModel {

    FirestoreRepository repo = new FirestoreRepository();
    MutableLiveData<Boolean> passMatch = new MutableLiveData<>();

    public void init() {
    }

    public boolean validate(String email) {
        boolean isValid = repo.validate(email);
        return isValid;
    }

    public void registerUser(User user) {
        if (user.getPassword().equals(user.getConfirmPassword())) {
            if (this.validate(user.getEmail())) {
                // Access a Cloud Firestore instance
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                // Add user as a new document with a generated ID
                db.collection("users")
                        .add(user)
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
                        new CalendarFragment()).addToBackStack(null).commit();
            }
        }
    }
}
