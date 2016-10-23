package mobilesystems.mobilesensing.persistence;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.lang.ref.WeakReference;

import mobilesystems.mobilesensing.MainActivity;
import mobilesystems.mobilesensing.R;

/**
 * Created by Jesper on 23/10/2016.
 */

public class FragmentTransactioner {

    private static FragmentTransactioner fragmentTransactioner;

    public static FragmentTransactioner get() {
        if (fragmentTransactioner == null) {
            fragmentTransactioner = new FragmentTransactioner();
        }
        return fragmentTransactioner;
    }

    private FragmentTransactioner() {
    }

    public void transactFragments(FragmentActivity fragmentActivity, Fragment fragment, String backStackTag) {
        WeakReference<FragmentActivity> wrActivity = new WeakReference<>(fragmentActivity);
        final Activity activity = wrActivity.get();

        if (activity != null && !activity.isFinishing()) {
            if (fragment != null) {
                FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (backStackTag != null) {
                    fragmentTransaction.addToBackStack(backStackTag);
                }
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.commitAllowingStateLoss();
            }
        }
    }
}
