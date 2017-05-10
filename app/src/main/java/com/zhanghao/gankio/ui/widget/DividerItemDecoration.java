package com.zhanghao.gankio.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by 张浩 on 2016/10/25.
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private static final int [] ATTR={
        android.R.attr.listDivider
    };
    public static final int HORIZONTAL_LIST=LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST=LinearLayoutManager.VERTICAL;

    private Drawable mDivider;
    private  int mOrientation;
    private boolean drawLastOne;

    public DividerItemDecoration(Context context, int mOrientation,boolean drawLastOne) {
        this.drawLastOne=drawLastOne;
        final TypedArray array = context.obtainStyledAttributes(ATTR);
        mDivider = array.getDrawable(0);
        array.recycle();
        setOrientation(mOrientation);
    }


    public void setOrientation(int orientation) {
        if (orientation!=HORIZONTAL_LIST&&orientation!=VERTICAL_LIST){
            throw new IllegalArgumentException("方向参数错误！");
        }
        this.mOrientation = orientation;
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation==VERTICAL_LIST)
            DrawVertical(c,parent,state);
        else
            DrawHorizontal(c,parent,state);
    }

    //竖直方向
    private void DrawVertical(Canvas c, RecyclerView parent, RecyclerView.State state) {
        final int left=parent.getPaddingLeft();
        final int right=parent.getWidth()-parent.getPaddingRight();
        final int childCount;
        if (drawLastOne)
            childCount=parent.getChildCount()-1;
        else
            childCount=parent.getChildCount();
        for (int i=0;i<childCount;i++){
            final View child=parent.getChildAt(i);
            final RecyclerView.LayoutParams params= (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top=child.getBottom()+params.bottomMargin;
            final int bottom=top+mDivider.getIntrinsicHeight();
            mDivider.setBounds(left,top,right,bottom);
            mDivider.draw(c);

        }
    }



    //水平方向
    private void DrawHorizontal(Canvas c, RecyclerView parent, RecyclerView.State state) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }

    }
}
