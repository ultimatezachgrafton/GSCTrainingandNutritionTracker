package zachg.gsctrainingandnutritiontracker.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static androidx.constraintlayout.widget.Constraints.TAG;

// Deals with FirebaseAuth and keeps MVVM architecture nice and clean
public class FirebaseSource {

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    public void login(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "signInWithEmail:failure", task.getException());
                        }
                    }
                });
            auth.signInWithEmailAndPassword(email, password);
    }

    public void register(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                        }
                    }
                });
    }

    public void logout() {
        auth.signOut();
        //auth.getInstance().signOut();
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public FirebaseUser getCurrentUser() {
        if (auth.getCurrentUser() != null) {
            Log.d(TAG, "fuser not null: " + String.valueOf(auth.getCurrentUser()));
            return auth.getCurrentUser(); // User is signed in
        } else {
            Log.d(TAG, "fuser null: " + String.valueOf(auth.getCurrentUser()));
            return null; // No user is signed in
        }
    }

}