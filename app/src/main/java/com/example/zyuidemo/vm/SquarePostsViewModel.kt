package com.example.zyuidemo.vm

import androidx.lifecycle.MutableLiveData
import com.example.zyuidemo.base.BaseViewModel
import com.example.zyuidemo.beans.TestPostBean
import com.example.zyuidemo.beans.TestPostsListBean
import com.example.zyuidemo.constant.MmkvConstants
import com.example.zyuidemo.utils.MmkvUtils
import kotlin.random.Random

class SquarePostsViewModel : BaseViewModel() {

    private val activitiesRepository = SquarePostsRepository()

    var postsData = MutableLiveData<TestPostsListBean>()

    /**
     * 获取帖子网络数据
     */
    fun getPostsData() {
        val list = ArrayList<TestPostBean>()
        for (i in 0..10) {
            list.add(getTestList(i))
        }
        val testPostsListBean = TestPostsListBean(list)
        postsData.postValue(testPostsListBean)
        MmkvUtils.getInstance().putObject(MmkvConstants.KEY_SQUARE_POSTS_LIST, testPostsListBean)
    }

    var tagsData = arrayOf("iPhone12紫色", "特斯拉道歉", "苹果发布会", "欧超暂停", "比得兔2确认引进", "新冠疫苗")
    val imgs = arrayOf(
        "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2015667352,2458218422&fm=26&gp=0.jpg",
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20180510%2Fc861c0e9509546f98c25ef09419f1b81.gif&refer=http%3A%2F%2F5b0988e595225.cdn.sohucs.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1621562526&t=dcb454a38e3eaf66aa7d10bf0b5f41e3",
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2F1812.img.pp.sohu.com.cn%2Fimages%2Fblog%2F2009%2F11%2F18%2F18%2F8%2F125b6560a6ag214.jpg&refer=http%3A%2F%2F1812.img.pp.sohu.com.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1621564876&t=80dfc526b24d2925d01553e0194d8bc9",
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fyouimg1.c-ctrip.com%2Ftarget%2Ftg%2F035%2F063%2F726%2F3ea4031f045945e1843ae5156749d64c.jpg&refer=http%3A%2F%2Fyouimg1.c-ctrip.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1621564876&t=2203390bdfb43efb318dda220649263f",
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic16.nipic.com%2F20110908%2F1836425_172718025374_2.jpg&refer=http%3A%2F%2Fpic16.nipic.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1621564876&t=5df5373d98f81d1a4d537e4246241835",
        "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=4087057811,445331467&fm=26&gp=0.jpg",
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Ffile2.renrendoc.com%2Ffileroot_temp3%2F2021-3%2F22%2F6d6d4388-7021-4580-9c52-5676233033d2%2F6d6d4388-7021-4580-9c52-5676233033d23.gif&refer=http%3A%2F%2Ffile2.renrendoc.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1621564876&t=48ccc32324197a4d4f0034ddd264121b",
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimage1.nphoto.net%2Fnews%2Fimage%2F201307%2F084a057c5177ae78.jpg&refer=http%3A%2F%2Fimage1.nphoto.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1621564876&t=f259f7a2fee33b45aa64457433883a94",
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fi1.sinaimg.cn%2FIT%2F2010%2F0419%2F201041993511.jpg&refer=http%3A%2F%2Fi1.sinaimg.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1621564876&t=e7148fd5d684794e09d823d92f549028",
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fcdn.duitang.com%2Fuploads%2Fitem%2F201408%2F19%2F20140819011654_ThLAM.thumb.700_0.png&refer=http%3A%2F%2Fcdn.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1621564876&t=b624391d0ee4f028175ed8141c7f013d",
        )

    private fun getTestList(pos: Int): TestPostBean {
        val pictures = ArrayList<String>()

        val tags = ArrayList<String>()

        for (i in 0..getRandom(imgs.size)) {
            pictures.add(imgs[getRandom(imgs.size)])
        }
        for (i in 0..getRandom(tagsData.size)) {
            tags.add(tagsData[getRandom(tagsData.size)])
        }
        return TestPostBean(
            0,
            pictures,
            "我是title${getRandom(Int.MAX_VALUE)}",
            "nick$pos",
            "https://dss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2271338977,1611087163&fm=26&gp=0.jpg",
            0,
            getRandom(Int.MAX_VALUE),
            getRandom(Int.MAX_VALUE),
            getRandom(Int.MAX_VALUE),
            "${getRandom(Int.MAX_VALUE)}",
            "${getRandom(Int.MAX_VALUE)}",
            "${getRandom(Int.MAX_VALUE)}",
            "上海",
            "url",
            tags,
            "20${getRandom(21)}-4-${getRandom(30)} " +
                    "${getRandom(24)}:${getRandom(60)}:${getRandom(60)}"
        )
    }

    private fun getRandom(size: Int) = (0 until size).random()

    /**
     * 获取帖子缓存数据
     */
    fun getCachePostsData(pageIndex: Int) {
        val squareListBean = MmkvUtils.getInstance()
            .getObject(MmkvConstants.KEY_SQUARE_POSTS_LIST, TestPostsListBean::class.java)
        if (squareListBean != null) {
            postsData.postValue(squareListBean)
        }
        getPostsData()
    }

}