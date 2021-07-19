package com.example.libcommon.widget.floatview;


import android.view.MotionEvent;

/**
 * @author  zhangyong
 */
public interface FloatViewListener {

    default void onRemove(FloatView floatView){};

    default void onClick(FloatView floatView){};

    default void onTouch(MotionEvent event){};
}
