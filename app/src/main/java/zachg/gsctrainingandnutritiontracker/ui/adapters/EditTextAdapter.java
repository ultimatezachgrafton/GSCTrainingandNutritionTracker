package zachg.gsctrainingandnutritiontracker.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;

public class EditTextAdapter {

    @BindingAdapter({"entries", "layout", "extra"})
    public static <T, V> void setEntries(ViewGroup viewGroup,
                                         T[] entries, int layoutId,
                                         Object extra) {
        viewGroup.removeAllViews();
        if (entries != null) {
            LayoutInflater inflater = (LayoutInflater)
                    viewGroup.getContext()
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (int i = 0; i < entries.length; i++) {
                T entry = entries[i];
                ViewDataBinding binding = DataBindingUtil
                        .inflate(inflater, layoutId, viewGroup, true);
                binding.setVariable(BR.data, entry);
                binding.setVariable(BR.extra, extra);
            }
        }
    }
}
