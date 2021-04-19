package com.example.zyuidemo.router

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.zyuidemo.beans.UserInfoBean
import com.example.zyuidemo.utils.MmkvUtils

@Route(path = ServicePath.LOGIN_SERVICE)
class LoginService : ILoginService {

    var userInfoBean: UserInfoBean? = null
    var mContext: Context? = null
    var webStateKey: String? = null

    override fun setUserInfo(userInfoBean: UserInfoBean) {
        this.userInfoBean = userInfoBean
        MmkvUtils.getInstance().putObject(UserInfoBean.USER_KEY, userInfoBean)
    }

    override fun setWebKey(key: String?) {
        webStateKey = key
    }

    override fun getUserInfo(): UserInfoBean? {
        if (userInfoBean == null) {
            userInfoBean = MmkvUtils.getInstance().getObject(UserInfoBean.USER_KEY, UserInfoBean::class.java)
        }
        return userInfoBean
    }

    override fun isLogin(): Boolean {
        return userInfoBean != null
    }

    override fun signOut() {
        userInfoBean = null
        MmkvUtils.getInstance().removeObject(UserInfoBean.USER_KEY)
    }


    override fun init(context: Context?) {
        mContext = context
    }


}