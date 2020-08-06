package com.naccoro.wask.datepicker.wheel;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import java.util.Arrays;


/**
 * Modified by: WheelView - wangjie
 * link : https://github.com/wangjiegulu/WheelView
 *
 * Email: jaeryo2357@naver.com
 * Date: 8/5/20.
 */
public class WheelDatePicker extends NestedScrollView {

    private static final String[] TEST_PLANETS = new String[]{"Mercury", "Venus", "Earth", "Mars", "Jupiter", "Uranus", "Neptune", "Pluto"};

    private LinearLayout parent;
    WheelRecyclerView yearRecycler;
    WheelRecyclerView monthRecycler;
    WheelRecyclerView dayRecycler;

    public WheelDatePicker(Context context) {
        super(context);
        init(context);
    }

    public WheelDatePicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WheelDatePicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        parent = new LinearLayout(context);
        parent.setOrientation(LinearLayout.HORIZONTAL);
        parent.setGravity(Gravity.CENTER);
        parent.setWeightSum(3);

        this.addView(parent);


        yearRecycler = new WheelRecyclerView(context);
        monthRecycler = new WheelRecyclerView(context);
        dayRecycler = new WheelRecyclerView(context);

        //3개의 recyclerView를 동일한 크기로 정렬하기 위해 weight 값을 1로 설정
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, yearRecycler.getMaxHeight(), 1);

        parent.addView(yearRecycler, params);
        parent.addView(monthRecycler, params);
        parent.addView(dayRecycler, params);

        yearRecycler.setItemList(Arrays.asList(TEST_PLANETS));
        monthRecycler.setItemList(Arrays.asList(TEST_PLANETS));
        dayRecycler.setItemList(Arrays.asList(TEST_PLANETS));

        yearRecycler.scrollToPosition(6);
    }
}
