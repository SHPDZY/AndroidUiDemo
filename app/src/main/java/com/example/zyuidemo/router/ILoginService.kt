package com.example.zyuidemo.router


import com.alibaba.android.arouter.facade.template.IProvider
import com.example.zyuidemo.beans.UserInfoBean


interface ILoginService : IProvider {

    fun setUserInfo(userInfoBean: UserInfoBean)

    fun getUserInfo(): UserInfoBean?

    fun isLogin(): Boolean = false

    fun signOut()

    /**
     * 保存通知回调h5的key
     */
    fun setWebKey(key: String?) {}

}
