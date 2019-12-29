package com.yzx.demo.ui

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.yzx.demo.R
import com.yzx.demo.widget.qmui.QMUIContinuousNestedScrollLayout
import com.yzx.demo.widget.qmui.behavior.QMUIContinuousNestedTopAreaBehavior
import com.yzx.demo.widget.qmui.view.QMUIContinuousNestedTopDelegateLayout
import com.yzx.demo.widget.qmui.view.QMUIContinuousNestedTopWebView
import kotlinx.android.synthetic.main.activity_head_web.*


/**
 * @author yzx
 * @date 2019/12/8
 * Description
 */
class HeadWebActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_head_web)

        val delegateLayout = QMUIContinuousNestedTopDelegateLayout(this)

        val topView = layoutInflater.inflate(
            R.layout.topview, delegateLayout, false
        )
        delegateLayout.headerView = topView
        val webView = QMUIContinuousNestedTopWebView(this)
        delegateLayout.delegateView = webView

        val matchParent = ViewGroup.LayoutParams.MATCH_PARENT
        val topLp = CoordinatorLayout.LayoutParams(
            matchParent, matchParent
        )
        topLp.behavior = QMUIContinuousNestedTopAreaBehavior(this)
        scrollView.setTopAreaView(delegateLayout, topLp)

        webView.loadUrl("https://mp.weixin.qq.com/s/zgfLOMD2JfZJKfHx-5BsBg")

        scrollView.addOnScrollListener(object : QMUIContinuousNestedScrollLayout.OnScrollListener {
            override fun onScroll(
                topCurrent: Int,
                topRange: Int,
                offsetCurrent: Int,
                offsetRange: Int,
                bottomCurrent: Int,
                bottomRange: Int
            ) {
                smartRefreshLayout.setEnableRefresh(topCurrent == 0)
                Log.i(
                    "ygl", String.format(
                        "topCurrent = %d; topRange = %d; " +
                                "offsetCurrent = %d; offsetRange = %d; " +
                                "bottomCurrent = %d, bottomRange = %d",
                        topCurrent, topRange, offsetCurrent, offsetRange, bottomCurrent, bottomRange
                    )
                )
            }

            override fun onScrollStateChange(newScrollState: Int, fromTopBehavior: Boolean) {

            }
        })
    }
}