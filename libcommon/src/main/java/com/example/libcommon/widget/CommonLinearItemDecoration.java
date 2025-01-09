package com.example.libcommon.widget;


import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SizeUtils;

public class CommonLinearItemDecoration extends RecyclerView.ItemDecoration {

    private int space, firstSpace, lastSpace;
    private boolean setFirst;
    private boolean setLast;
    private boolean isHorizontal;

    public CommonLinearItemDecoration(int space) {
        this.space = SizeUtils.dp2px(space);
    }

    public CommonLinearItemDecoration(int space, Boolean setTopAndBottom) {
        this.space = SizeUtils.dp2px(space);
        if (setTopAndBottom) {
            this.firstSpace = this.space;
            this.lastSpace = this.space;
            this.setFirst = true;
            this.setLast = true;
        }
    }

    public CommonLinearItemDecoration(int space, Boolean setTop, Boolean setBottom) {
        this.space = SizeUtils.dp2px(space);
        if (setTop) {
            this.firstSpace = this.space;
            this.setFirst = true;
        }
        if (setBottom) {
            this.lastSpace = this.space;
            this.setLast = true;
        }
    }

    public CommonLinearItemDecoration(int space, int firstSpace, int lastSpace) {
        this.space = SizeUtils.dp2px(space);
        this.firstSpace = SizeUtils.dp2px(firstSpace);
        this.lastSpace = SizeUtils.dp2px(lastSpace);
        this.setFirst = true;
        this.setLast = true;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (outRect == null) return;
        isHorizontal = parent.getLayoutManager().canScrollHorizontally();
        int itemCount = parent.getAdapter().getItemCount();
        int itemPosition = parent.getChildAdapterPosition(view);
        boolean isLast = itemPosition == itemCount - 1;

        //设置第一个item
        if (itemPosition == 0 && setFirst) {
            setTopLeft(outRect, firstSpace);
        }
        if (isLast) {
            //设置最后一个item
            setBottomRight(outRect, setLast ? lastSpace : 0);
        } else {
            //设置中间的item
            setBottomRight(outRect, space);
        }
    }

    private void setBottomRight(Rect outRect, int space) {
        if (isHorizontal) {
            outRect.right = space;
        } else {
            outRect.bottom = space;
        }
    }

    private void setTopLeft(Rect outRect, int space) {
        if (isHorizontal) {
            outRect.left = space;
        } else {
            outRect.top = space;
        }
    }
}
