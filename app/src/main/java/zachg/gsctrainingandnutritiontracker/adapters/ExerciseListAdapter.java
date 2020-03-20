package zachg.gsctrainingandnutritiontracker.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.models.Exercise;
import zachg.gsctrainingandnutritiontracker.models.Workout;

import static androidx.annotation.InspectableProperty.ValueType.COLOR;

public class ExerciseListAdapter extends FirestoreRecyclerAdapter<Exercise, ExerciseListAdapter.ExerciseViewHolder> {

    public class ExerciseViewHolder extends RecyclerView.ViewHolder {

        EditText etExerciseName, etReps, etWeightUsed;

        ExerciseViewHolder(View itemView) {
            super(itemView);
            etExerciseName = itemView.findViewById(R.id.etExerciseName);
            etReps = itemView.findViewById(R.id.etReps);
            etWeightUsed = itemView.findViewById(R.id.etWeightUsed);
        }
    }

    private ArrayList<Exercise> exercises;

    public ExerciseListAdapter(@NonNull FirestoreRecyclerOptions<Exercise> options) {
        super(options);
    }

    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_exercise_item, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ExerciseViewHolder holder, int i, @NonNull Exercise exercise) {
        Exercise e = exercises.get(i);

        holder.etExerciseName.setHint(R.string.enter_exercise_name);
        holder.etExerciseName.setHintTextColor(Color.WHITE);
        holder.etReps.setHint(R.string.enter_reps);
        holder.etReps.setHintTextColor(Color.WHITE);
        holder.etWeightUsed.setHint(R.string.enter_weight);
        holder.etWeightUsed.setHintTextColor(Color.WHITE);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}