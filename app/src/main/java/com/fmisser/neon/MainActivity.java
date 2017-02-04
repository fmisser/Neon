package com.fmisser.neon;

import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.fmisser.neon.common.BadgeDrawable;
import com.fmisser.neon.common.Utils;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);
        initBottomNav();
    }

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
                        setBadge(R.id.bottom_nav_discover, 999);
                        break;
                    case R.id.bottom_nav_discover:
                        setBadge(R.id.bottom_nav_discover, 12);
                        break;
                    case R.id.bottom_nav_device:
                        setBadge(R.id.bottom_nav_discover, 8);
                        break;
                    case R.id.bottom_nav_mine:
                        setBadge(R.id.bottom_nav_discover, 0);
                        break;
                }
                return true;
            }
        });
    }

    private void setBadge(int id, int count) {
        MenuItem mallItem = mBottomNavigationView.getMenu().findItem(id);
        LayerDrawable icon = (LayerDrawable) mallItem.getIcon();
        BadgeDrawable.setBadgeCount(icon, R.id.ic_badge, count);
        mallItem.setIcon(icon);
    }
}
