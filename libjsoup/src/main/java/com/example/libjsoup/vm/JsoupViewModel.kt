package com.example.libjsoup.vm

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import com.example.libcommon.beans.ItemSearchBean
import com.example.libcommon.beans.ItemSearchNavBean
import com.example.libcommon.utils.launch
import com.example.libcore.mvvm.BaseViewModel
import org.jsoup.Jsoup

/**
 * @desc:
 */
class JsoupViewModel : BaseViewModel() {

    val htmlData = MutableLiveData<MutableList<ItemSearchBean>>()
    val navData = MutableLiveData<MutableList<ItemSearchNavBean>>()

    init {

    }

    fun parseHtmlWeb(html: String) {
        launch({
            val parse = Jsoup.parse(html)
        })
    }
    fun parseHtml(search: String) {
        launch({
            val document = Jsoup.connect(search)
                .header(
                    "Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3"
                )
                .header("Accept-Encoding", "gzip, deflate")
                .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                .header("Cache-Control", "no-cache")
                .header("Pragma", "no-cache")
                .header("Proxy-Connection", "keep-alive")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.131 Safari/537.36")
                .get()
            LogUtils.d("document ${document.html()}")
            val data = mutableListOf<ItemSearchBean>()
            val bAlgo = document.select("li.b_algo")
            for (element in bAlgo) {
                val title = element.select("a[href]").text()
                val href = element.select("cite").text()
                val img = element.select("img.AC-faviconT").attr("href")
                data.add(ItemSearchBean(title, href, img))
                LogUtils.d("title $title href $href img $img")
            }
            val navData = mutableListOf<ItemSearchNavBean>()
            var navElement = document.select("ul.sb_pagF").first()
            if (navElement == null){
                navElement = document.select("li.b_pag").first()
            }
            val sb_bp = navElement.select("a.sb_bp")
            for (child in sb_bp) {
                var currentPage = child.select("a.sb_pagS").first()
                var isCurrent = true
                if (currentPage == null) {
                    currentPage = child.select("a.b_widePag").first()
                    isCurrent = false
                }
                val pageUrl = currentPage.attr("href")
                val pageIndex = currentPage.text()
                navData.add(ItemSearchNavBean(pageIndex, "https://cn.bing.com$pageUrl", isCurrent))
                LogUtils.d("pageUrl $pageUrl pageIndex $pageIndex isCurrent $isCurrent")
            }
            this.htmlData.postValue(data)
            this.navData.postValue(navData)
        })
    }

}