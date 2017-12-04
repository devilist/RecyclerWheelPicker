/*
 * Copyright  2017  zengp
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.devilist.recyclerwheelpicker.widget;

import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by zengp on 2017/11/22.
 */

class WheelPickerLayoutManager extends LinearLayoutManager {

    private RecyclerWheelPicker mRecyclerView;
    int mVerticalOffset = 0; // offset in vertical orientation when scrolling
    int mItemHeight = 0;
    private int mMaxOverScrollOffset = 0;
    boolean mIsOverScroll = false; // whether no item cross the center
    private SparseArray<Rect> mItemAreas; // record all visible child display area
    private final float MILLISECONDS_PER_INCH = 50f; // scroll speed

    public WheelPickerLayoutManager(RecyclerWheelPicker recyclerView) {
        super(recyclerView.getContext());
        this.mRecyclerView = recyclerView;
        setOrientation(VERTICAL);
    }

    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(VERTICAL);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        if (getItemCount() <= 0 || state.isPreLayout()) return;

        // layout algorithm:
        // 1.scrap all attached views
        // 2. record all visible items display area
        // 3. fill views

        detachAndScrapAttachedViews(recycler);

        View first = recycler.getViewForPosition(0);
        measureChildWithMargins(first, 0, 0);
        int childWidth = getDecoratedMeasuredWidth(first);
        int childHeight = getDecoratedMeasuredHeight(first);

        mItemHeight = childHeight;
        mMaxOverScrollOffset = getVerticalSpace() / 2 + childHeight / 2;

        // record all the visible items rect
        mItemAreas = new SparseArray<>();
        // first item layout in center
        int topToCenterOffset = getVerticalSpace() / 2 - childHeight / 2;
        int offsetHeight = getPaddingTop() + topToCenterOffset;
        for (int i = 0; i < getItemCount(); i++) {
            Rect rect = new Rect(getPaddingLeft(), offsetHeight, getPaddingLeft() + childWidth, offsetHeight + childHeight);
            mItemAreas.put(i, rect);
            offsetHeight += childHeight;
        }

        // fill views
        fillView(recycler, state);
    }

    private void fillView(RecyclerView.Recycler recycler, RecyclerView.State state) {

        if (getItemCount() <= 0 || state.isPreLayout()) return;

        // the visible area for the RecyclerView
        Rect displayArea = new Rect(0, mVerticalOffset, getHorizontalSpace(),
                getVerticalSpace() + mVerticalOffset);

        // remove invisible child
        Rect rect = new Rect();
        for (int i = 0; i < getChildCount(); i++) {
            View item = getChildAt(i);
            rect.set(getDecoratedLeft(item), getDecoratedTop(item),
                    getDecoratedRight(item), getDecoratedBottom(item));
            if (!Rect.intersects(displayArea, rect)) {
                removeAndRecycleView(item, recycler);
            }
        }

        // add visible child
        for (int i = 0; i < getItemCount(); i++) {
            Rect area = mItemAreas.get(i);
            if (Rect.intersects(displayArea, area)) {
                View child = recycler.getViewForPosition(i);
                addView(child);
                measureChildWithMargins(child, 0, 0);
                Rect childRect = new Rect();
                calculateItemDecorationsForChild(child, childRect);
                layoutDecorated(child, area.left, area.top - mVerticalOffset,
                        area.right, area.bottom - mVerticalOffset);
            }
        }
    }

    @Override
    public boolean canScrollVertically() {
        return mRecyclerView.isScrollEnabled();
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        // scrap all attached views and re-layout by scrolling distance
        detachAndScrapAttachedViews(recycler);
        mIsOverScroll = false;
        // no items, vertical offset must be 0;
        if (getItemCount() == 0) {
            dy = 0;
            mVerticalOffset = 0;
        }
        // scroll to top bound ; dy < 0; mVerticalOffset < 0
        if (dy < 0) {
            if (mVerticalOffset + dy <= -mItemHeight)
                mIsOverScroll = true;
            if (mVerticalOffset + dy <= -mMaxOverScrollOffset) {
                dy = 5;
                int offset = -(mVerticalOffset + mMaxOverScrollOffset);
                if (getChildCount() == 0) dy += offset;
            }
        }
        // scroll to bottom bound ; dy > 0; mVerticalOffset > 0
        if (dy > 0) {
            int verticalOffset = mVerticalOffset - (getItemCount() - 1) * mItemHeight;
            if (verticalOffset + dy >= 0)
                mIsOverScroll = true;
            if (dy > 0 && verticalOffset + dy >= mMaxOverScrollOffset) {
                dy = -5;
                int offset = -(verticalOffset - mMaxOverScrollOffset);
                if (getChildCount() == 0) dy += offset;
            }
        }
        offsetChildrenVertical(-dy);
        fillView(recycler, state);
        mVerticalOffset += dy;
        return dy;
    }


    void checkVerticalOffsetBound() {
        int totalItemCount = getItemCount();
        if (totalItemCount == 0) mVerticalOffset = 0;
        else if (mVerticalOffset > (totalItemCount - 1) * mItemHeight) {
            mVerticalOffset = (totalItemCount - 1) * mItemHeight;
        } else if (mVerticalOffset < 0)
            mVerticalOffset = 0;
    }

    int findCenterItemPosition() {
        if (getItemCount() == 0)
            return 0;
        if (mRecyclerView.getChildCount() == 0)
            return RecyclerView.NO_POSITION;

        int first = findFirstCompletelyVisibleItemPosition();
        int last = findLastCompletelyVisibleItemPosition();
        if (first == last) return first;
        for (int i = first; i <= last; i++) {
            RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForAdapterPosition(i);
            if (null == holder) continue;
            View child = holder.itemView;
            if (null == child) continue;
            int centerY = getVerticalSpace() / 2;
            int childCenterY = child.getTop() + child.getHeight() / 2;
            if (Math.abs(centerY - childCenterY) <= 1) return i;
        }
        return RecyclerView.NO_POSITION;
    }

    private int getVerticalSpace() {
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }

    private int getHorizontalSpace() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    void scrollTargetPositionToCenter(final int position, int itemHeight) {
        if (position < 0) return;
        int distance = itemHeight * position;
        mRecyclerView.smoothScrollBy(0, distance);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, final int position) {
        LinearSmoothScroller smoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
            }

            @Nullable
            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return WheelPickerLayoutManager.this.computeScrollVectorForPosition(targetPosition);
            }
        };

        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }
}
