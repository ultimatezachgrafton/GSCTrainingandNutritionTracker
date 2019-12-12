package zachg.gsctrainingandnutritiontracker.ui.adapters;

import android.util.Log;
import android.widget.CalendarView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingListener;

import java.util.Calendar;

public class CalendarViewBindingAdapter {
    public String TAG = "CalendarViewBindingAdapter";

    @BindingAdapter(value = {"android:onSelectedDayChange", "android:dateAttrChanged"},
            requireAll = false)
    public static void setListeners(CalendarView view, final CalendarView.OnDateChangeListener onDayChange,
                                    final InverseBindingListener attrChange) {
        if (attrChange == null) {
            view.setOnDateChangeListener(onDayChange);
        } else {
            view.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(CalendarView view, int year, int month,
                                                int dayOfMonth) {
                    if (onDayChange != null) {
                        onDayChange.onSelectedDayChange(view, year, month, dayOfMonth);
                    }
                    Calendar instance = Calendar.getInstance();
                    instance.set(year, month, dayOfMonth);
                    view.setDate(instance.getTimeInMillis());
                    attrChange.onChange();
                }
            });
        }
    }
}
