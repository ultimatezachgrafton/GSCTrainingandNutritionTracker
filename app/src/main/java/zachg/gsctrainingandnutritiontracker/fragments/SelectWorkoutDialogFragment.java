package zachg.gsctrainingandnutritiontracker.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.models.Workout;

public class SelectWorkoutDialogFragment extends DialogFragment {

    private static final String TAG = "MyCustomDialog";

    public interface OnInputListener{
        void sendInput(String input);
    }
    public OnInputListener mOnInputListener;

    private Workout workout = new Workout();
    private ArrayList<Workout> workouts = new ArrayList<>();
    private ArrayList<TextView> workoutTextViewArray = new ArrayList<>();

    private TextView actionCancel;

    private int totalTvs = 0;

    public SelectWorkoutDialogFragment(ArrayList<Workout> workouts) {
        this.workouts = workouts;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_select_workout, container, false);
        actionCancel = view.findViewById(R.id.action_cancel);

        LinearLayout ll = new LinearLayout(getContext());
        ll = view.findViewById(R.id.generateTvs);

        // generate TextViews
        while (totalTvs < workouts.size() ) {
            addLine(ll, workouts.get(totalTvs));
        }

        actionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing dialog");
                getDialog().dismiss();
            }
        });

//        mActionOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: capturing input");

//                String input = mInput.getText().toString();
//                if(!input.equals("")){
//
//                    //Easiest way: just set the value
//                    ((MainActivity)getActivity()).mInputDisplay.setText(input);
//
//                }
                //"Best Practice" but it takes longer
//                mOnInputListener.sendInput(input);

//                getDialog().dismiss();
//            }
//        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputListener = (OnInputListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
        }
    }

    public void addLine(LinearLayout ll, Workout workout) {
        TextView tv = new TextView(getContext());
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(p);
        tv.setTextColor(Color.WHITE);
        tv.setText(workouts.get(totalTvs).getWorkoutTitle());
        tv.setId(totalTvs + 1);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setTextSize(18);

        // Add to the View
        ll.addView(tv);

        workoutTextViewArray.add(totalTvs, tv);
        totalTvs++;
        Log.d(TAG, "addLine3" + totalTvs);
    }
}
