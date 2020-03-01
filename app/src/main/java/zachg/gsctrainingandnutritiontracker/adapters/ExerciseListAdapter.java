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
import zachg.gsctrainingandnutritiontracker.models.Exercise;

public class ExerciseListAdapter extends FirestoreRecyclerAdapter<Exercise, ExerciseListAdapter.ExerciseViewHolder> {
    private OnItemClickListener listener;
    private Exercise exercise = new Exercise();
    public String TAG = "ExerciseListAdapter";

    // Listens for a Firestore query
    public ExerciseListAdapter(@NonNull FirestoreRecyclerOptions<Exercise> options) {
        super(options);
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_exercise_item, parent, false);

        return new ExerciseViewHolder(v);
    }

    // UserViewHolder is the class that defines the views that hold the User data
    class ExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView tvExerciseName;
        TextView tvExerciseReps;
        TextView tvExerciseWeight;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExerciseName = itemView.findViewById(R.id.tvExerciseName);
            tvExerciseReps = itemView.findViewById(R.id.tvReps);
            tvExerciseWeight = itemView.findViewById(R.id.tvWeightUsed);

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
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position, Exercise exercise) {
        Log.d(TAG, "onBindVH");
        holder.tvExerciseName.setText(exercise.getExerciseName());
        holder.tvExerciseReps.setText( exercise.getReps());
        holder.tvExerciseWeight.setText(exercise.getExerciseWeight());
    }

}