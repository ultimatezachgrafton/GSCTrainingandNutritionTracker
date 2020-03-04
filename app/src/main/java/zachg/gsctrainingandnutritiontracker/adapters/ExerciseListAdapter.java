package zachg.gsctrainingandnutritiontracker.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.models.Exercise;

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ExerciseViewHolder> {

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

    public ExerciseListAdapter(ArrayList<Exercise> arrayList) {
        exercises = arrayList;
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_exercise_item, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExerciseViewHolder holder, int position) {
        Exercise e = exercises.get(position);

        holder.etExerciseName.setHint("Exericse name");
        holder.etReps.setHint("Reps");
        holder.etWeightUsed.setHint("Weight");
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}