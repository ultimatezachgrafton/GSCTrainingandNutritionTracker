package zachg.gsctrainingandnutritiontracker.models;

import com.google.firebase.firestore.FirebaseFirestore;

// Connects to the firebase database for document and collection data, keeps MVVM tidy
public class FirebaseFirestoreSource {

    public FirebaseFirestore db = FirebaseFirestore.getInstance();

}
