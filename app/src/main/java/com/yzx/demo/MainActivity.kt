package com.yzx.demo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.angcyo.dsladapter.DslAdapter
import com.angcyo.dsladapter.DslItemDecoration
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.yzx.demo.ui.HeadWebActivity
import com.yzx.demo.ui.coroutine.CoroutineActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var dslAdapter: DslAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dslAdapter = DslAdapter()
        recyclerView.apply {
            adapter = dslAdapter
            addItemDecoration(DslItemDecoration())
        }
        dslAdapter.apply {
            listOf("HeadWebView", "Coroutine")
                .forEachIndexed { index, data ->
                    MainItem()() {
                        if (index != 0) {
                            itemTopInsert = 10
                        }
                        title = data
                        onItemClick = {
                            nav(data)
                        }
                    }
                }
        }

        PermissionUtils.permission(PermissionConstants.STORAGE)
            .rationale {

            }
            .callback(object : PermissionUtils.FullCallback {
                override fun onGranted(permissionsGranted: MutableList<String>?) {

                }

                override fun onDenied(
                    permissionsDeniedForever: MutableList<String>?,
                    permissionsDenied: MutableList<String>?
                ) {

                }
            })
            .request()
    }

    private fun nav(title: String) {
        val clz: Class<*> =
            when (title) {
                "HeadWebView" -> HeadWebActivity::class.java
                "HeadViewPager" -> HeadWebActivity::class.java
                "Coroutine" -> CoroutineActivity::class.java
                else -> HeadWebActivity::class.java
            }
        startActivity(Intent(this, clz))
    }

}
