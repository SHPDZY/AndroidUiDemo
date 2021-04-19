package com.example.zyuidemo.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.zyuidemo.R
import com.example.zyuidemo.router.PagePath
import com.example.zyuidemo.router.RouterConstants

@Route(path = PagePath.NEVBASE_FRAGMENT_CONTAINER)
open class FragmentContainerActivity : AppCompatActivity() {
    companion object {
        const val TAG = "FragmentContainer"
    }

    @Autowired(name = RouterConstants.EXTRA_KEY_FRAGMENT_PATH)
    @JvmField
    var fragmentPath: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_container)
        ARouter.getInstance().inject(this)
        addFragment()
    }

    private fun addFragment() {
        if (fragmentPath.isNullOrEmpty()) {
            Log.i(FragmentContainerActivity.TAG, "fragment path is null, return")
            finish()
            return
        }
        var fragment = supportFragmentManager.findFragmentByTag(fragmentPath)
        if (fragment == null) {
            fragment = ARouter.getInstance().build(fragmentPath).navigation() as Fragment?
            if (fragment == null) {
                Log.e(FragmentContainerActivity.TAG, "no fragment from " + fragmentPath)
                finish()
                return
            }
            copyExtras2Fragment(fragment)

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.vg_fragment, fragment, fragmentPath)
            fragmentTransaction.commitAllowingStateLoss()
        }

    }

    fun copyExtras2Fragment(fragment: Fragment) {
        var arguments = fragment.arguments
        if (arguments == null) {
            arguments = Bundle()
            fragment.arguments = arguments
        }
        var extras = intent.extras
        extras?.run {
            arguments.putAll(extras)
        }
    }

    override fun onBackPressed() {
        var fragment = supportFragmentManager.findFragmentByTag(fragmentPath)
        if (fragment == null) {
            super.onBackPressed()
            return
        }
        if (fragment is IPage && fragment.onBack()) {
//            super.onBackPressed()
            return
        }
        super.onBackPressed()
    }
}