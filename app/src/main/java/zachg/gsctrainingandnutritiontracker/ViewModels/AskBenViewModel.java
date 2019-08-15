package zachg.gsctrainingandnutritiontracker.ViewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.Models.Message;
import zachg.gsctrainingandnutritiontracker.Repositories.FirestoreRepository;

import static zachg.gsctrainingandnutritiontracker.Repositories.FirestoreRepository.db;

public class AskBenViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Message>> mMessages;
    private FirestoreRepository mRepo;
    private MutableLiveData<Boolean> mIsUpdating = new MutableLiveData<>();
    private CollectionReference messageColRef = db.collection("messages");
    private DocumentReference messageDocRef = messageColRef.document("clientName");//, currentSelectedUser.getClientName());

    public void init() {
        if (mMessages != null) {
            return;
        }
        mRepo = FirestoreRepository.getInstance();
    }

    public void sendMessage() {

        Message message = new Message();
        messageDocRef.set(message)
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
