package zachg.gsctrainingandnutritiontracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.models.Exercise;

public class ExerciseClickAdapter  extends RecyclerView.Adapter<ExerciseClickAdapter.ExerciseClickViewHolder> {

    public class ExerciseClickViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView text1, text2, title, subtitle;

        ExerciseClickViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(android.R.id.text1);
            subtitle = (TextView) itemView.findViewById(android.R.id.text2);
        }

        @Override
        public void onClick(View v) {
            // The user may not set a click listener for list items, in which case our listener
            // will be null, so we need to check for this
            if (onEntryClickListener != null) {
                onEntryClickListener.onEntryClick(v, getLayoutPosition());
            }
        }
    }

    private ArrayList<Exercise> customObjects;

    public ExerciseClickAdapter(ArrayList<Exercise> arrayList) {
        customObjects = arrayList;
    }

    @Override
    public int getItemCount() {
        return customObjects.size();
    }

    @Override
    public ExerciseClickViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ExerciseClickViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExerciseClickViewHolder holder, int position) {
        Exercise object = customObjects.get(position);

        String firstText = object.getExerciseName();
        String secondText = object.getExerciseName();

//        holder.text1.setText(firstText);
//        holder.text2.setText(secondText);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    private OnEntryClickListener onEntryClickListener;

    public interface OnEntryClickListener {
        void onEntryClick(View view, int position);
    }

    public void setOnEntryClickListener(OnEntryClickListener onEntryClickListener) {
        onEntryClickListener = onEntryClickListener;
    }
}
