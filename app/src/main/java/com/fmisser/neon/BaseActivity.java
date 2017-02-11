package com.fmisser.neon;

import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.List;

/**
 * Date: 2017/2/8.
 * Author: fmisser
 * Description: 基础Activity
 */


public abstract class BaseActivity extends AppCompatActivity {

    /**
     * snack bar holder view
     * 用于显示snack bar, 基于activity本身或者fragment调用统一使用该函数,方便处理一些视图的偏移,fab等.
     */
    public abstract View getSnackBarHolderView();

    /**
     * 淡入淡出切换页面
     */
    public void switchToFragment(@IdRes int containerViewId, Fragment to) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null &&
                !fragmentList.isEmpty()) {
            for (Fragment fragment : fragmentList) {
                if (fragment.isVisible()) {
                    transaction.hide(fragment);
                }
            }
        }

        if (!to.isAdded()) {
            transaction.add(containerViewId, to)
                    .commit();
        } else {
            transaction.show(to)
                    .commit();
        }
    }
}
