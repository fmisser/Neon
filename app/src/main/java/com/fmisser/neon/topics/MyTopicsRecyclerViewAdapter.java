package com.fmisser.neon.topics;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fmisser.neon.R;
import com.fmisser.neon.topics.dummy.DummyContent.DummyItem;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link TopicsFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyTopicsRecyclerViewAdapter extends RecyclerView.Adapter<MyTopicsRecyclerViewAdapter.ViewHolder> {

    private final List<DummyItem> mValues;
    private final TopicsFragment.OnListFragmentInteractionListener mListener;
    //retains insert order, use LinkedHashMap
    private Map<Integer, DummyItem> mRemovedValues = new LinkedHashMap<>();

    public MyTopicsRecyclerViewAdapter(List<DummyItem> items, TopicsFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_topics, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mPicture, holder.mItem);
                }
            }
        });
    }

    public void add(int pos, DummyItem item) {
        if (pos < 0) {
            mValues.add(0, item);
            notifyItemInserted(0);
        } else if (pos >= mValues.size()) {
            add(item);
        } else {
            mValues.add(pos, item);
            notifyItemInserted(pos);
        }
    }

    public void add(DummyItem item) {
        mValues.add(item);
        notifyItemInserted(mValues.size() - 1);
    }

    /**
     * 删除
     */
    public void remove(int pos) {
        if (pos >= 0 && pos < getItemCount()) {
            DummyItem item = mValues.remove(pos);
            mRemovedValues.put(pos, item);
//            notifyDataSetChanged();
            notifyItemRemoved(pos);
        }
    }

    /**
     * 复原上次删除
     */
    public void recoverLastDeleted() {

        Iterator<Map.Entry<Integer, DummyItem>> iterator = mRemovedValues.entrySet().iterator();
        Map.Entry<Integer, DummyItem> tail = null;
        while (iterator.hasNext()) {
            tail = iterator.next();
        }
        if (tail != null) {
            mRemovedValues.remove(tail.getKey());
            add(tail.getKey(), tail.getValue());
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public DummyItem mItem;

        public final ImageView mPicture;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);

            mPicture = (ImageView) view.findViewById(R.id.picture);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
