package com.fmisser.neon;

import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

import com.fmisser.neon.common.BadgeDrawable;
import com.fmisser.neon.common.ScrollFloatingActionButtonBehavior;
import com.fmisser.neon.common.Utils;
import com.fmisser.neon.topicdetail.TopicDetailActivity;
import com.fmisser.neon.topics.TopicsFragment;
import com.fmisser.neon.topics.dummy.DummyContent;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends BaseActivity implements TopicsFragment.OnListFragmentInteractionListener {

    private static final long EXIT_MILLIS = 2000L;
    private long lastTime = 0L;

    private CoordinatorLayout mMainLayout;
    private BottomNavigationView mBottomNavigationView;
    private BottomBar mBottomBar;
    private FrameLayout mFragmentContent;
    private FloatingActionButton mFabAdd;
    private int mFabAddBottomMargin;
    private Interpolator mScrollInterpolator = new DecelerateInterpolator(2.0f);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        /**
         * 设置半透明status bar & navigation bar
         * 同时设置Fullscreen
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorTranslucent));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorTranslucent));
            getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }

        mMainLayout = (CoordinatorLayout) findViewById(R.id.activity_main);
//        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);
//        initBottomNav();

        //Bottom bar
        mBottomBar = (BottomBar) findViewById(R.id.bottom_bar);
        mBottomBar.setDefaultTab(R.id.tab_mall);
        initBottomBar();


        /**
         * Fab
         */
        mFragmentContent = (FrameLayout) findViewById(R.id.topics_content);
        mFabAdd = (FloatingActionButton) findViewById(R.id.fab_add);

        //fab offset
        int navigationBarHeight = Utils.getNavigationBarHeight(this);
        int bottomBarHeight = getResources().getDimensionPixelSize(R.dimen.bottom_bar_height);
        mFabAddBottomMargin = getResources().getDimensionPixelSize(R.dimen.topics_fab_vertical_margin) + navigationBarHeight + bottomBarHeight;
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mFabAdd.getLayoutParams();
        layoutParams.bottomMargin = mFabAddBottomMargin;
        //fab behavior
        layoutParams.setBehavior(new ScrollFloatingActionButtonBehavior() {
            @Override
            public void onHide() {
                mFabAdd.animate()
                        .translationY(mFabAddBottomMargin + mFabAdd.getHeight())
                        .setInterpolator(mScrollInterpolator);
            }

            @Override
            public void onShow() {
                mFabAdd.animate()
                        .translationY(0)
                        .setInterpolator(mScrollInterpolator);
            }
        });
        mFabAdd.setLayoutParams(layoutParams);
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
                        TopicsFragment topicsFragment = (TopicsFragment) getSupportFragmentManager().findFragmentById(R.id.topics_content);
                        if (topicsFragment != null) {
                            topicsFragment.reselect();
                        }
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

    @Override
    public View getSnackBarHolderView() {
        return mMainLayout;
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
            Snackbar.make(getSnackBarHolderView(), "再按一次返回键退出", (int) EXIT_MILLIS).show();
        } else {
            //退到后台,不结束应用
            moveTaskToBack(false);
        }
    }

    @Override
    public void onListFragmentInteraction(View view, DummyContent.DummyItem item) {
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(view, view.getWidth()/2, view.getHeight()/2, 0, 0);
        Intent intent = new Intent(this, TopicDetailActivity.class);
        ActivityCompat.startActivity(this, intent, optionsCompat.toBundle());
    }
}
