package zachg.gsctrainingandnutritiontracker.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.models.Exercise;
import zachg.gsctrainingandnutritiontracker.models.Workout;

import static androidx.annotation.InspectableProperty.ValueType.COLOR;

public class ExerciseListAdapter extends FirestoreRecyclerAdapter<Exercise, ExerciseListAdapter.ExerciseViewHolder> {

    private Exercise exercise = new Exercise();
    private ArrayList<Exercise> exerciseArrayList = new ArrayList<>();
    private static final String TAG = "ExerciseListAdapter";

    public class ExerciseViewHolder extends RecyclerView.ViewHolder {

        EditText etExerciseName, etReps, etWeightUsed;

        ExerciseViewHolder(View itemView) {
            super(itemView);
            etExerciseName = itemView.findViewById(R.id.etExerciseName);
            etReps = itemView.findViewById(R.id.etReps);
        }
    }

    public ExerciseListAdapter(@NonNull FirestoreRecyclerOptions<Exercise> options) {
        super(options);
    }

    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_exercise_item, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position, @NonNull Exercise exercise) {
        holder.etExerciseName.setHint(R.string.enter_exercise_name);
        holder.etExerciseName.setHintTextColor(Color.WHITE);
        holder.etReps.setHint(R.string.enter_reps);
        holder.etReps.setHintTextColor(Color.WHITE);

        holder.etExerciseName.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                String text = s.toString();
                exercise.setExerciseName(text);
                exerciseArrayList.add(position, exercise);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        holder.etReps.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                String text = s.toString();
                exercise.setExerciseReps(text);
                exerciseArrayList.add(position, exercise);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    // Add a method for easy access to your weights.
    public ArrayList<Exercise> getExercises() {
        return exerciseArrayList;
    }

}