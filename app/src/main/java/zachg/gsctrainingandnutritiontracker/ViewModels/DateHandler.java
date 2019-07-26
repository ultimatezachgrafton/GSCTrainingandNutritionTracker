package zachg.gsctrainingandnutritiontracker.ViewModels;

import android.util.Log;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class DateHandler {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static ArrayList<Date> fetchDates(final ArrayList<Date> mDates, final CollectionReference colRef) {

        // might not need - check for DRY when completing date-based ordering for reps and msgs
        // works with both reportHandler and msgHandler
        Query dateQuery = colRef;
        getDateOptions(colRef);

        // iterates through reports or msgs, orders by Date
        dateQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Date date = new Date();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        date = doc.toObject(Date.class);
                        mDates.add(date);
                    }
                } else {
                    Log.e(TAG, "failed to make mDates");
                }
            }
        });
        return mDates;
    }

    public static FirestoreRecyclerOptions<Date> getDateOptions(Query query) {
        FirestoreRecyclerOptions<Date> dateOptions = new FirestoreRecyclerOptions.Builder<Date>()
                .setQuery(query, Date.class)
                .build();

        return dateOptions;
    }
}