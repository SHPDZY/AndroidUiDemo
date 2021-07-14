package com.example.zyuidemo.vm


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.libcore.mvvm.BaseViewModel
import com.example.libcommon.beans.UserInfoBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class RegisterViewModel : BaseViewModel() {

    val smsCodeLiveData: MutableLiveData<SmsBean> = MutableLiveData()
    val registerLiveData: MutableLiveData<UserInfoBean> = MutableLiveData()
    val timerLiveData: MutableLiveData<Int> = MutableLiveData()

    fun getSms(phone: String) {
        timer()
        val smsBean = SmsBean()
        smsCodeLiveData.postValue(smsBean.apply {
            code = 0
        })
    }

    fun timer() {
        viewModelScope.launch(Dispatchers.Main) {
            for (i in 59 downTo 0) {
                timerLiveData.value = i
                delay(1000)
            }
        }
    }


}

class SmsBean {
    var code: Int = -1
    var message: String = ""
}

