package com.fmisser.neon.discover;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.fmisser.neon.R;
import com.fmisser.neon.common.Utils;
import com.fmisser.neon.discover.dummy.DummyContent;
import com.fmisser.neon.discover.dummy.DummyContent.DummyItem;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class TopicsFragment extends Fragment {

    private CardView mSearchBar;
    private int mSearchBarTopMargin = 0;
    private RecyclerView mRecyclerView;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TopicsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TopicsFragment newInstance(int columnCount) {
        TopicsFragment fragment = new TopicsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_topics_list, container, false);

        mSearchBar = (CardView) root.findViewById(R.id.search_bar);

        //沉浸式全屏需要偏移status bar的高度
        int statusBarHeight = Utils.getStatusBarHeight(getContext());
        mSearchBarTopMargin = getContext().getResources().getDimensionPixelSize(R.dimen.search_bar_vertical_margin) + statusBarHeight;
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mSearchBar.getLayoutParams();
        layoutParams.topMargin = mSearchBarTopMargin;
        mSearchBar.setLayoutParams(layoutParams);

        // Set the adapter
        Context context = root.getContext();
        mRecyclerView = (RecyclerView) root.findViewById(R.id.list);
        if (mColumnCount <= 1) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        mRecyclerView.setAdapter(new MyTopicsRecyclerViewAdapter(DummyContent.ITEMS, mListener));
        mRecyclerView.addOnScrollListener(new HidingScrollerListener() {
            @Override
            public void onHide() {

                mSearchBar.animate()
                        .translationY(-mSearchBar.getHeight() - mSearchBarTopMargin)
                        .setInterpolator(new DecelerateInterpolator(2.0f));
            }

            @Override
            public void onShow() {
                mSearchBar.animate()
                        .translationY(0)
                        .setInterpolator(new DecelerateInterpolator(2.0f));
            }
        });

        //沉浸式全屏需要偏移status bar的高度
        int recyclerViewTopPadding = getContext().getResources().getDimensionPixelSize(R.dimen.topics_list_padding_top) + statusBarHeight;
        mRecyclerView.setPadding(0, recyclerViewTopPadding, 0, 0);
        mRecyclerView.setClipToPadding(false);

        return root;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnListFragmentInteractionListener) {
//            mListener = (OnListFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //再次点击回到顶部
    public void reselect() {
        mRecyclerView.smoothScrollToPosition(0);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }


    /**
     * 滚动隐藏监听器, 为了实现Toolbar 滚动时显示或者隐藏, 参考 @see https://mzgreen.github.io/2015/02/15/How-to-hideshow-Toolbar-when-list-is-scroling(part1)/
     */
    public abstract class HidingScrollerListener extends RecyclerView.OnScrollListener {
        private static final int HIDE_THRESHOLD = 1;
        private int mScrolled = 0;
        private boolean mVisible = true;

        public abstract void onHide();
        public abstract void onShow();

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            super.onScrolled(recyclerView, dx, dy);
            int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

            //当第一个item仍然可见的时候,显示toolbar或者不执行隐藏操作
            if (firstVisibleItem == 0) {
                if (!mVisible) {
                    onShow();
                    mVisible = true;
                }
            } else {

                if (mScrolled > HIDE_THRESHOLD && mVisible) {
                    onHide();
                    mVisible = false;
                    mScrolled = 0;
                } else if (mScrolled < -HIDE_THRESHOLD && !mVisible) {
                    onShow();
                    mVisible = true;
                    mScrolled = 0;
                }
            }

            if ((mVisible && dy > 0) ||
                    (!mVisible && dy< 0)) {
                mScrolled += dy;
            }
        }
    }
}
