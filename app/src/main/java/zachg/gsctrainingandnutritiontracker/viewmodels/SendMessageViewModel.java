package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import zachg.gsctrainingandnutritiontracker.models.Message;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class SendMessageViewModel extends ViewModel {

    private FirestoreRepository mRepo = new FirestoreRepository();

    public void init() {
        mRepo = FirestoreRepository.getInstance();
    }

    public void sendMessage() {
        Message message = new Message();
        mRepo.db.collection("messages").document("username")
                .set(message)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("mReports", "DocumentSnapshot added with ID: ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("mReports", "Error writing document", e);
                    }
                });
    }
}
