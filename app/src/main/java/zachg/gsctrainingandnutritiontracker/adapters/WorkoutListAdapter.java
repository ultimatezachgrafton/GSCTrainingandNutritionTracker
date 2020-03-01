package zachg.gsctrainingandnutritiontracker.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.models.Workout;

public class WorkoutListAdapter extends FirestoreRecyclerAdapter<Workout, WorkoutListAdapter.WorkoutViewHolder> {
    private OnItemClickListener listener;
    private Workout workoutList = new Workout();
    public String TAG = "WorkoutListAdapter";

    // Listens for a Firestore query
    public WorkoutListAdapter(@NonNull FirestoreRecyclerOptions<Workout> options) {
        super(options);
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_exercise_item, parent, false);

        return new WorkoutViewHolder(v);
    }

    // UserViewHolder is the class that defines the views that hold the User data
    class WorkoutViewHolder extends RecyclerView.ViewHolder {
        TextView tvWorkoutTitle;
        TextView tvWorkoutDay;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWorkoutTitle = itemView.findViewById(R.id.tvWorkoutTitle);
            tvWorkoutDay = itemView.findViewById(R.id.tvWorkoutDay);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position, Workout workout) {
        Log.d(TAG, "onBindVH");
        holder.tvWorkoutTitle.setText(workout.getWorkoutTitle());
        holder.tvWorkoutDay.setText(workout.getWorkoutDay());
    }

}
