package com.example.zyuidemo.ui.fragment

import android.annotation.SuppressLint
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.drakeet.multitype.MultiTypeAdapter
import com.example.libcommon.router.PagePath
import com.example.libcore.multitype.BaseViewBinder
import com.example.libcore.multitype.vu.BaseVu
import com.example.libcore.mvvm.BaseVMFragment
import com.example.zyuidemo.R
import com.example.zyuidemo.databinding.FragmentJktBinding
import com.example.zyuidemo.databinding.ItemJtkViewBinding
import kotlin.random.Random


@Route(path = PagePath.GROUP_UI_JTK_FRAGMENT)
class JTKFragment : BaseVMFragment<FragmentJktBinding>(R.layout.fragment_jkt),
    View.OnClickListener {

    val items = arrayListOf<Any>()
    val adapter = MultiTypeAdapter(items)

    @SuppressLint("NotifyDataSetChanged")
    override fun initView() {
        binding.click = this
        binding.run {
            adapter.register(JtkData::class.java, BaseViewBinder(JtkView::class.java))
            items.add(JtkData("2024-11-08 19:45:${getRandomS()}", false))
            items.add(JtkData("2024-11-08 19:12:${getRandomS()}", true))
            items.add(JtkData("2024-11-08 08:32:${getRandomS()}", false))
            items.add(JtkData("2024-11-08 08:07:${getRandomS()}", true))

            items.add(JtkData("2024-11-07 19:40:${getRandomS()}", false))
            items.add(JtkData("2024-11-07 19:11:${getRandomS()}", true))
            items.add(JtkData("2024-11-07 08:36:${getRandomS()}", false))
            items.add(JtkData("2024-11-07 08:12:${getRandomS()}", true))

            items.add(JtkData("2024-11-06 19:45:${getRandomS()}", false))
            items.add(JtkData("2024-11-06 19:15:${getRandomS()}", true))
            items.add(JtkData("2024-11-06 08:35:${getRandomS()}", false))
            items.add(JtkData("2024-11-06 08:16:${getRandomS()}", true))

            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    override fun onClick(p0: View?) {

    }

    fun getRandomS(): String {
        return String.format("%02d", Random.nextInt(0, 59))
    }

}

class JtkView : BaseVu<ItemJtkViewBinding, JtkData>(), View.OnClickListener {


    override fun getLayoutId(): Int {
        return R.layout.item_jtk_view
    }

    override fun bindData(data: JtkData) {
        binding.tvDate.text = data.date
        binding.tv3.text = if (data.isIn) "-0.00元" else "-4.00元"
    }

    override fun onClick(v: View?) {

    }
}

data class JtkData(
    val date: String,
    val isIn: Boolean
)