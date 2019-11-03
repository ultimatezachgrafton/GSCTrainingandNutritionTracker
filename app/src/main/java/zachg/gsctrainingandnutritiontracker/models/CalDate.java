package zachg.gsctrainingandnutritiontracker.models;

import android.widget.DatePicker;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableInt;

import java.util.Calendar;

public class CalDate extends BaseObservable {

    public final ObservableInt year = new ObservableInt();
    public final ObservableInt month = new ObservableInt();
    public final ObservableInt day = new ObservableInt();

    public CalDate() {
        Calendar calendar = Calendar.getInstance();
        year.set(calendar.get(Calendar.YEAR));
        month.set(calendar.get(Calendar.MONTH));
        day.set(calendar.get(Calendar.DAY_OF_MONTH));
    }

    public void dateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year.set(year);
        this.month.set(monthOfYear);
        this.day.set(dayOfMonth);
    }
}
