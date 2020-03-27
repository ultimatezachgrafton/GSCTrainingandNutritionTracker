package zachg.gsctrainingandnutritiontracker.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

public class LogOutReceiver extends BroadcastReceiver {

    private static final String TAG = "LogOutReceiver";

//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        /**snip **/
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("com.package.ACTION_LOGOUT");
//        registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Log.d("onReceive","Logout in progress");
//                //At this point you should start the login activity and finish this one
//                finish();
//            }
//        }, intentFilter);
//        //** snip **//
//    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Received broadcast intent: " + intent.getAction());
    }
}
