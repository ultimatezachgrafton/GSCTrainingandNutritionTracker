package zachg.gsctrainingandnutritiontracker.adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.models.Exercise;

public class ExerciseForViewOnlyListAdapter extends FirestoreRecyclerAdapter<Exercise, ExerciseForViewOnlyListAdapter.ExerciseViewHolder> {

    private static final String TAG = "ExerciseListAdapter";;

    public class ExerciseViewHolder extends RecyclerView.ViewHolder {

        TextView tvExerciseName, tvReps, tvWeightUsed;

        ExerciseViewHolder(View itemView) {
            super(itemView);
            tvExerciseName = itemView.findViewById(R.id.tvExerciseName);
            tvReps = itemView.findViewById(R.id.tvReps);
        }
    }

    public ExerciseForViewOnlyListAdapter(@NonNull FirestoreRecyclerOptions<Exercise> options) {
        super(options);
    }

    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_exercise_item_report_only, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position, @NonNull Exercise exercise) {
        holder.tvExerciseName.setText(exercise.getExerciseName());
        holder.tvReps.setText(exercise.getExerciseReps());
    }

}