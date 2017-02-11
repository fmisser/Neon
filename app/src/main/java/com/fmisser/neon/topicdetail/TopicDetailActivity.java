package com.fmisser.neon.topicdetail;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fmisser.neon.BaseActivity;
import com.fmisser.neon.R;
import com.fmisser.neon.common.Utils;

import me.henrytao.smoothappbarlayout.SmoothAppBarLayout;

public class TopicDetailActivity extends BaseActivity {

    private CoordinatorLayout mRootLayout;
    private SwipeRefreshLayout mRefreshLayout;
    private SmoothAppBarLayout mAppBarLayout;
    private CollapsingToolbarLayout mCollapsingLayout;
    private Toolbar mToolbar;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }

        /**
         * 设置透明status bar
         * 同时设置Fullscreen
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        mRootLayout = (CoordinatorLayout) findViewById(R.id.activity_topic_detail);

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        int startOffset = getResources().getDimensionPixelSize(R.dimen.topic_detail_app_bar_height) - mRefreshLayout.getProgressCircleDiameter();
        mRefreshLayout.setProgressViewOffset(false, startOffset, startOffset + mRefreshLayout.getProgressViewEndOffset());

        mCollapsingLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_bar);

        mAppBarLayout = (SmoothAppBarLayout) findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (appBarLayout.getTotalScrollRange() + verticalOffset == 0) {
                    mCollapsingLayout.setTitle("fmisser");
                    isShow = true;
                } else if (isShow) {
                    //carefully there should a space between double quote otherwise it wont work
                    mCollapsingLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });


        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.inflateMenu(R.menu.menu_topic_detail_tool_bar);
        //设置overflow icon
//        mToolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_more_vert_grey_600_24dp));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                ActivityCompat.finishAfterTransition(TopicDetailActivity.this);
            }
        });
        int statusBarHeight = Utils.getStatusBarHeight(this);
        CollapsingToolbarLayout.LayoutParams layoutParams = (CollapsingToolbarLayout.LayoutParams) mToolbar.getLayoutParams();
        layoutParams.topMargin = statusBarHeight;
        mToolbar.setLayoutParams(layoutParams);

        mImageView = (ImageView) findViewById(R.id.image);
    }

    @Override
    public View getSnackBarHolderView() {
        return mRootLayout;
    }
}
