package com.example.libcommon.widget;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SizeUtils;

public class CommonGridItemDecoration extends RecyclerView.ItemDecoration {

    private int horizontalSpace;
    private int topSpace;
    private int bottomSpace;
    private int verticalSpace;
    private int horizontalCenterSpace;
    private int verticalCenterSpace;

    public CommonGridItemDecoration(int topSpace, int verticalSpace, int horizontalSpace) {
        this.topSpace = SizeUtils.dp2px(topSpace);
        this.verticalSpace = SizeUtils.dp2px(verticalSpace);
        this.horizontalCenterSpace = SizeUtils.dp2px(horizontalSpace);
    }

    public CommonGridItemDecoration(int topSpace, int bottomSpace, int verticalSpace, int horizontalSpace) {
        this.topSpace = SizeUtils.dp2px(topSpace);
        this.bottomSpace = SizeUtils.dp2px(bottomSpace);
        this.verticalSpace = SizeUtils.dp2px(verticalSpace);
        this.horizontalCenterSpace = SizeUtils.dp2px(horizontalSpace);
    }

    public CommonGridItemDecoration(int verticalSpace, int horizontalCenterSpace) {
        this.verticalSpace = SizeUtils.dp2px(verticalSpace);
        this.horizontalCenterSpace = SizeUtils.dp2px(horizontalCenterSpace);
    }

    public CommonGridItemDecoration(float verticalSpace, float horizontalCenterSpace) {
        this.verticalSpace = SizeUtils.dp2px(verticalSpace);
        this.horizontalCenterSpace = SizeUtils.dp2px(horizontalCenterSpace);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (outRect == null) return;
        int position = parent.getChildAdapterPosition(view); // item position
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        int spanCount = getSpanCount(layoutManager);
        int itemCount = layoutManager.getItemCount();

        int rowCount = getRowCountOfPos(spanCount, itemCount);
        int currentRow = getRowCountOfPos(spanCount, position + 1);

        int rowPos = position - ((currentRow - 1) * spanCount);

        if (position < spanCount) {
            outRect.top = topSpace;
        } else {
            outRect.top = 0;
        }
        if (currentRow == rowCount) {
            outRect.bottom = bottomSpace;
        } else if (itemCount > spanCount) {
            outRect.bottom = verticalSpace;
        } else {
            outRect.bottom = 0;
        }
        if (horizontalCenterSpace > 0) {
            if (rowPos == 0) {
                outRect.left = 0;
                outRect.right = horizontalCenterSpace;
            } else if (rowPos == spanCount - 1) {
                outRect.left = horizontalCenterSpace;
                outRect.right = 0;
            } else {
                outRect.left = horizontalCenterSpace;
                outRect.right = horizontalCenterSpace;
            }
        } else {
            outRect.left = horizontalSpace;
            outRect.right = horizontalSpace;
        }
    }

    private int getRowCountOfPos(int spanCount, int index) {
        int residue = index % spanCount;
        int rowCount = index / spanCount;
        if (residue > 0) {
            rowCount = rowCount + 1;
        }
        return rowCount;
    }

    /**
     * 获取列数
     *
     * @return 列数
     */
    private int getSpanCount(RecyclerView.LayoutManager layoutManager) {
        int spanCount = -1;
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }

}
