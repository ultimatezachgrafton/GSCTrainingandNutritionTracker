package zachg.gsctrainingandnutritiontracker.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.R;

public class PagerActivity extends AppCompatActivity {

    // ReportPagerActivity is the Activity for Reports utilizing ViewPager to allow the user to swipe left and right on reports

    // Creates explicit intent, and pass in a Serializable key value to create new Reports
    private static final String EXTRA_REPORT_ID = "zachg.gsctrainingandnutritiontracker";

    private ViewPager mViewPager;
    public static Intent newIntent(Context packageContext, int reportId) {
        Intent intent = new Intent(packageContext, PagerActivity.class);
        intent.putExtra(EXTRA_REPORT_ID, reportId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_pager);

        int reportId = getIntent().getIntExtra(EXTRA_REPORT_ID, 0);

        mViewPager = (ViewPager) findViewById(R.id.report_view_pager);

        // get Reports from Firestore
        //mReports = Report.getReports(this).getReports();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Report report = reports.get(position);
                return new Fragment();
            }

            @Override
            public int getCount() {
                return reports.size();
            }
        });

        for (int i = 0; i < reports.size(); i++) {
            if (reports.get(i).getReportId().equals(reportId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
