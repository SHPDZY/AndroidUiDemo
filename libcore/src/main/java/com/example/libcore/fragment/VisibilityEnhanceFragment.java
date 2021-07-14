package com.example.libcore.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public abstract class VisibilityEnhanceFragment extends Fragment implements IFragment {
    protected boolean isFirst = true;
    boolean isLastVisible = false;
    private boolean hidden = false;
    private boolean isResuming = false;
    private boolean isViewEnable = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isLastVisible = false;
        hidden = false;
        isFirst = true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isViewEnable = true;
        return null;
    }

    /**
     * Fragment 是否在前台。
     */
    private boolean isResuming() {
        return isResuming;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewEnable = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        isResuming = true;
        tryToChangeVisibility(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        isResuming = false;
        tryToChangeVisibility(false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        onHiddenChangedClient(hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        setUserVisibleHintClient(isVisibleToUser);
    }

    private void setUserVisibleHintClient(boolean isVisibleToUser) {
        tryToChangeVisibility(isVisibleToUser);
        if (isAdded()) {
            // 当Fragment不可见时，其子Fragment也是不可见的。因此要通知子Fragment当前可见状态改变了。
            List<Fragment> fragments = getChildFragmentManager().getFragments();
            for (Fragment fragment : fragments) {
                if (fragment instanceof VisibilityEnhanceFragment) {
                    ((VisibilityEnhanceFragment) fragment).setUserVisibleHintClient(isVisibleToUser);
                }
            }
        }
    }

    void onHiddenChangedClient(boolean hidden) {
        this.hidden = hidden;
        tryToChangeVisibility(!hidden);
        if (isAdded()) {
            List<Fragment> fragments = getChildFragmentManager().getFragments();
            if (fragments != null && fragments.size() > 0) {
                for (Fragment fragment : fragments) {
                    if (fragment instanceof VisibilityEnhanceFragment) {
                        ((VisibilityEnhanceFragment) fragment).onHiddenChangedClient(hidden);
                    }
                }
            }
        }
    }

    private void tryToChangeVisibility(boolean tryToShow) {
        // 上次可见
        if (isLastVisible) {
            if (tryToShow) {
                return;
            }
            if (!isFragmentVisible()) {
                onFragmentPause();
                isLastVisible = false;
            }
            // 上次不可见
        } else {
            boolean tryToHide = !tryToShow;
            if (tryToHide) {
                return;
            }
            if (isFragmentVisible()) {
                onFragmentResume(isFirst, isViewEnable);
                isLastVisible = true;
                isFirst = false;
            }
        }
    }

    /**
     * Fragment是否可见
     */
    public boolean isFragmentVisible() {
        return isResuming()
                && getUserVisibleHint()
                && !hidden;

    }

    public boolean isViewEnable() {
        return isViewEnable;
    }

    /**
     * Fragment 可见时回调
     *
     * @param isFirst      是否是第一次显示
     * @param isViewEnable Fragment 的 View 被回收，但是Fragment实例仍在。
     */
    public void onFragmentResume(boolean isFirst, boolean isViewEnable) {

    }

    /**
     * Fragment 不可见时回调
     */
    public void onFragmentPause() {

    }
}
