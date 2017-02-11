package com.fmisser.neon;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Date: 2017/2/8.
 * Author: fmisser
 * Description: 基础 fragment
 */

public class BaseFragment extends Fragment {

    public View getSnackBarHolderView() {
        if (getActivity() instanceof BaseActivity) {
            return ((BaseActivity) getActivity()).getSnackBarHolderView();
        } else {
            throw new RuntimeException("请基于BaseActivity和BaseFragment来设计.");
        }
    }
}
