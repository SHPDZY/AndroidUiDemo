package com.example.zyuidemo.base.multitype;

import com.drakeet.multitype.ItemViewBinder;
import com.example.zyuidemo.base.multitype.vu.Vu;

/**
 *
 */
public interface ICompParser {

    boolean canParse(String type);

    BaseViewBinder[] getViewBinders();

    Class<? extends ItemViewBinder<?, ?>> getItemViewBinder(String type);

    Class<? extends ItemViewBinder<?, ?>> getNoMatchBinder();

    Vu getCompVu(String type);
}
