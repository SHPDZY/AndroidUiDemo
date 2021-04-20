package com.example.zyuidemo.ui

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.*
import com.example.zyuidemo.R
import com.example.zyuidemo.base.BaseVMFragment
import com.example.zyuidemo.beans.UserInfoBean
import com.example.zyuidemo.databinding.FragmentRegisterBinding
import com.example.zyuidemo.router.PagePath
import com.example.zyuidemo.router.loginService
import com.example.zyuidemo.utils.KeyBoardClick2HideUtil
import com.example.zyuidemo.vm.RegisterViewModel
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.noober.background.drawable.DrawableCreator
import kotlinx.coroutines.*

@Route(path = PagePath.REGISTER)
class RegisterFragment : BaseVMFragment<FragmentRegisterBinding>(R.layout.fragment_register),
    View.OnClickListener, TextView.OnEditorActionListener, android.text.TextWatcher,
    View.OnFocusChangeListener {

    private var loginEnable: Drawable? = null
    private var loginUnEnable: Drawable? = null
    private var isEditPhone: Boolean = false
    private var isEditCode: Boolean = false

    private val registerModel by lazy { ViewModelProvider(requireActivity()).get(RegisterViewModel::class.java) }

    private var xPopup: BasePopupView? = null


    @SuppressLint("SetTextI18n")
    override fun startObserve() {
        registerModel.smsCodeLiveData.observe(this, {
            if (it.code != 0) {
                binding.tvObtain.isEnabled = true
                ToastUtils.showShort(it.message)
            }
        })
        registerModel.registerLiveData.observe(this, {
            dismissDialog()
            if (it.code == 0) {
                if (it.userNo.isNotEmpty()) {
                    val activityList = ActivityUtils.getActivityList()
                    activityList[activityList.size - 1].finish()
                    ARouter.getInstance().build(PagePath.MAIN).navigation(activity)
                    GlobalScope.launch {
                        delay(300)
                        finish()
                    }
                }
            } else {
                ToastUtils.showShort(it.message)
            }
        })
        registerModel.timerLiveData.observe(this, {
            when {
                it > 0 -> {
                    binding.tvTime.isVisible = true
                    binding.tvTime.text = "$it"
                    binding.tvObtain.text = "’之后重新获取"
                    binding.tvObtain.isEnabled = false
                }
                else -> {
                    binding.tvTime.isVisible = false
                    binding.tvObtain.text = "重新获取"
                    binding.tvObtain.isEnabled = true
                }
            }
        })
    }

    override fun onBack(): Boolean {
        return false
    }


    override fun initData() {
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun initView() {
        loginEnable = DrawableCreator.Builder().setCornersRadius(SizeUtils.dp2px(30f).toFloat())
            .setStrokeWidth(SizeUtils.dp2px(2f).toFloat())
            .setStrokeColor(resources.getColor(R.color.colorSlBrand))
            .build()

        loginUnEnable = DrawableCreator.Builder().setCornersRadius(SizeUtils.dp2px(30f).toFloat())
            .setStrokeWidth(SizeUtils.dp2px(2f).toFloat())
            .setStrokeColor(resources.getColor(R.color.colorSlGray))
            .build()
        startAnimation(0, binding.tvWelcome, true)
        startAnimation(600, binding.layoutPhone, false)
        binding.etPhoneNum.run {
            setOnEditorActionListener(this@RegisterFragment)
            addTextChangedListener(this@RegisterFragment)
            onFocusChangeListener = this@RegisterFragment
        }

        binding.etSecurityCode.run {
            setOnEditorActionListener(this@RegisterFragment)
            addTextChangedListener(this@RegisterFragment)
            onFocusChangeListener = this@RegisterFragment
        }
        activity?.let {
            KeyboardUtils.registerSoftInputChangedListener(it) { height ->
                if (height > 0) {
                    return@registerSoftInputChangedListener
                }
                if (isEditPhone && checkPhoneNum(binding.etPhoneNum.text.toString().trim())) {
                    showCodeLayoutAndGetSms()
                } else if (isEditCode && checkVerifyCode(
                        binding.etSecurityCode.text.toString().trim()
                    )
                ) {
                    postVerifySms()
                }
            }
        }
        binding.cbCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            changeLoginBtnStatus()
        }
//        binding.tvAgreement.paint.flags = Paint.UNDERLINE_TEXT_FLAG
//        binding.tvAgreement.paint.isAntiAlias = true
        binding.click = this
    }

    /**
     * 展示验证码布局and获取验证码
     */
    private fun showCodeLayoutAndGetSms() {
        startAnimation(0, binding.layoutGetCode, true)
        startAnimation(600, binding.layoutSecurityCode, false)
        if (binding.tvObtain.isEnabled) {
            registerModel.getSms(binding.etPhoneNum.text.toString())
        }
    }

    /**
     * 开始动画，
     */
    private fun startAnimation(delay: Long, view: View, isLeft: Boolean) {
        if (view.isVisible) return
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.Default) {
                delay(delay)
            }
            view.isVisible = true
            val animationSet = AnimationSet(true)
            val alphaAnimation = AlphaAnimation(0f, 1f)
            alphaAnimation.duration = 500
            val pivotX = if (isLeft) 0f else view.measuredWidth.toFloat()
            val pivotY = view.measuredHeight.toFloat()
            val scaleAnimation = ScaleAnimation(0f, 1f, 0f, 1f, pivotX, pivotY)
            scaleAnimation.duration = 500
            scaleAnimation.fillAfter = true
            scaleAnimation.fillBefore = false
            animationSet.addAnimation(scaleAnimation)
            animationSet.addAnimation(alphaAnimation)
            view.startAnimation(animationSet)
        }
    }

    private fun changeLoginBtnStatus() {
        binding.run {
            val phone = etPhoneNum.text.toString().trim()
            val code = etSecurityCode.text.toString().trim()
            val cb = cbCheck.isChecked
            btLogin.isEnabled = phone.isNotEmpty() && code.isNotEmpty() && cb
            if (btLogin.isEnabled) {
                btLogin.background = loginEnable
                btLogin.setTextColor(resources.getColor(R.color.colorSlBrand))
            } else {
                btLogin.background = loginUnEnable
                btLogin.setTextColor(resources.getColor(R.color.colorSlGray))
            }
        }
    }

    override fun onClick(v: View?) {
        if (v == null) {
            return
        }
        if (!DebouncingUtils.isValid(v)) {
            return
        }
        when (v.id) {
            R.id.bt_login -> {
                postRegister()
            }
            R.id.tv_obtain -> {
                val phone = binding.etPhoneNum.text.toString().trim()
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.showShort("请输入手机号码")
                    return
                }
                val mobileExact = RegexUtils.isMobileExact(phone)
                if (!mobileExact) {
                    ToastUtils.showShort("手机格式有误请输入正确的手机号码")
                    return
                }
                registerModel.getSms(phone)
            }
            R.id.iv_phone_clear -> {
                binding.etPhoneNum.text?.clear()
            }
            R.id.iv_close -> {
                finish()
            }
        }
    }

    private fun postVerifySms() {
        val phone = binding.etPhoneNum.text.toString().trim()
        val code = binding.etSecurityCode.text.toString().trim()
        if (!checkPhoneAndCode(phone, code)) return
        startAnimation(0, binding.layoutStart, true)
    }

    private fun postRegister() {
        val phone = binding.etPhoneNum.text.toString().trim()
        val code = binding.etSecurityCode.text.toString().trim()
        if (!checkPhoneAndCode(phone, code)) return
        if (!binding.cbCheck.isChecked) {
            ToastUtils.showShort("请勾选用户服务协议")
            return
        }
        showDialog()
        val userInfoBean = UserInfoBean()
        userInfoBean.accessToken = "token"
        userInfoBean.userNo = "no"
        userInfoBean.beanId = "id"
        userInfoBean.nickName = "nickName"
        loginService().setUserInfo(userInfoBean)
        GlobalScope.launch {
            delay(500)
            dismissDialog()
            ARouter.getInstance().build(PagePath.MAIN).navigation(activity)
            delay(300)
            finish()
        }
    }

    private fun checkPhoneAndCode(phone: String, code: String): Boolean {
        if (!checkPhoneNum(phone)) return false
        if (!checkVerifyCode(code)) return false
        return true
    }

    private fun checkPhoneNum(phone: String): Boolean {
        if (!TextUtils.isEmpty(phone)) {
            if (phone.length != 11 || !RegexUtils.isMobileExact(phone)) {
                ToastUtils.showShort("手机格式有误请输入正确的手机号码")
                return false
            }
            return true
        }
        ToastUtils.showShort("请输入手机号码")
        return false
    }

    private fun checkVerifyCode(code: String): Boolean {
        if (TextUtils.isEmpty(code)) {
            ToastUtils.showShort("请输入验证码")
            return false
        }
        return true
    }

    private fun showDialog() {
        xPopup = XPopup.Builder(activity)
            .asLoading()
            .show()
    }

    private fun dismissDialog() {
        xPopup?.smartDismiss()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        KeyBoardClick2HideUtil.click2Hide(view, this)
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if ((actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_SEARCH) && v != null) {
            KeyboardUtils.hideSoftInput(v)
            return true
        }
        return false
    }

    override fun afterTextChanged(s: Editable?) {
        changeLoginBtnStatus()
        if (!isEditPhone) return
        s?.run {
            val phone = s.trim()
            binding.ivPhoneClear.isInvisible = phone.isEmpty()
            if (phone.length == 11 && !RegexUtils.isMobileExact(phone)) {
                ToastUtils.showShort("手机格式有误请输入正确的手机号码")
            }
        }


    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        isEditPhone = hasFocus && v?.id == R.id.et_phone_num
        isEditCode = hasFocus && v?.id == R.id.et_security_code
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

}








