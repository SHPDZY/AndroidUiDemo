package com.example.libscan.router

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.libcommon.router.*

@Route(path = ServicePath.SCAN_SERVICE)
class ScanService : IScanService {
    override fun openScanActivity() {
        RouterUtils.navigation(PagePath.GROUP_SCAN_ACTIVITY)
    }

    override fun openAddActivity(code: String?) {
        RouterUtils.navigation(PagePath.GROUP_ADD_ACTIVITY + "?${RouterConstants.SCAN_CODE}=$code")
    }

    override fun init(context: Context?) {
    }

}