package com.fmisser.neon;

import android.graphics.drawable.LayerDrawable;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.fmisser.neon.common.BadgeDrawable;
import com.fmisser.neon.common.Utils;
import com.fmisser.neon.discover.TopicsFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final long EXIT_MILLIS = 2000L;
    private long lastTime = 0L;

    private CoordinatorLayout mMainLayout;
    private BottomNavigationView mBottomNavigationView;
    private BottomBar mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainLayout = (CoordinatorLayout) findViewById(R.id.activity_main);
//        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);
//        initBottomNav();

        mBottomBar = (BottomBar) findViewById(R.id.bottom_bar);
        mBottomBar.setDefaultTab(R.id.tab_mall);
        initBottomBar();
    }

    /**
     * @deprecated Support 25.1.1 BottomNavigationView实现仍然不完整,暂时丢弃.
     */
    @Deprecated
    private void initBottomNav() {
        //禁用 shift mode
        Utils.disableShiftMode(mBottomNavigationView);

        // 去除 badge 裁剪
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) mBottomNavigationView.getChildAt(0);
        for (int i=0; i< menuView.getChildCount(); i++) {
            BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(i);
            itemView.setClipChildren(false);
        }

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.bottom_nav_mall:

                        break;
                    case R.id.bottom_nav_discover:
                        TopicsFragment topicsFragment = (TopicsFragment) getSupportFragmentManager().findFragmentById(R.id.topics_content);
                        if (topicsFragment == null) {
                            topicsFragment = TopicsFragment.newInstance(1);
                        }
                        switchToFragment(R.id.topics_content, topicsFragment);
                        break;
                    case R.id.bottom_nav_device:

                        break;
                    case R.id.bottom_nav_mine:

                        break;
                }
                return true;
            }
        });
    }

    /**
     * @deprecated Support 25.1.1 BottomNavigationView实现仍然不完整,暂时丢弃.
     */
    @Deprecated
    private void setBadge(int id, int count) {
        MenuItem mallItem = mBottomNavigationView.getMenu().findItem(id);
        LayerDrawable icon = (LayerDrawable) mallItem.getIcon();
        BadgeDrawable.setBadgeCount(icon, R.id.ic_badge, count);
        mallItem.setIcon(icon);
    }

    private void initBottomBar() {
        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_mall:
                        break;
                    case R.id.tab_discover:
                        TopicsFragment topicsFragment = (TopicsFragment) getSupportFragmentManager().findFragmentById(R.id.topics_content);
                        if (topicsFragment == null) {
                            topicsFragment = TopicsFragment.newInstance(1);
                        }
                        switchToFragment(R.id.topics_content, topicsFragment);
                        break;
                    case R.id.tab_device:
                        break;
                    case R.id.tab_mine:
                        break;
                    default:
                        break;
                }
            }
        });

        mBottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_mall:
                        break;
                    case R.id.tab_discover:
                        break;
                    case R.id.tab_device:
                        break;
                    case R.id.tab_mine:
                        break;
                    default:
                        break;
                }
            }
        });
    }

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackKeyDown();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void onBackKeyDown() {
        long currMillis = System.currentTimeMillis();
        if (currMillis - lastTime > EXIT_MILLIS) {
            lastTime = currMillis;
            Snackbar.make(mMainLayout, "再按一次返回键退出", (int) EXIT_MILLIS)
                    .show();
        } else {
            //退到后台,不结束应用
            moveTaskToBack(false);
        }
    }
}
