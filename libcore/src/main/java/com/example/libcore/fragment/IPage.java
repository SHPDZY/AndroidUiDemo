package com.example.libcore.fragment;

public interface IPage {
    /**
     * 处理back事件。
     *
     * @return True: 表示已经处理; False: 没有处理，让基类处理。
     */
    default boolean onBack() {
        return false;
    }
}
