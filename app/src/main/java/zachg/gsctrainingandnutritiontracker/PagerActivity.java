package zachg.gsctrainingandnutritiontracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
import java.util.UUID;

public class PagerActivity extends AppCompatActivity implements ReportFragment.Callbacks {

    // ReportPagerActivity is the Activity for Reports utilizing ViewPager to allow the user to swipe left and right on reports

    // Creates explicit intent, and pass in a Serializable key value to create new Reports
    private static final String EXTRA_REPORT_ID = "zachg.gsctrainingandnutritiontracker";

    private ViewPager mViewPager;
    private List<Report> mReports;

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
        //mReports = ReportLab.get(this).getReports();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Report report = mReports.get(position);
                return ReportFragment.newInstance(report.getId());
            }

            @Override
            public int getCount() {
                return mReports.size();
            }
        });

        for (int i = 0; i < mReports.size(); i++) {
            if (mReports.get(i).getId().equals(reportId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    public void onReportUpdated(Report report) {
    }
}
