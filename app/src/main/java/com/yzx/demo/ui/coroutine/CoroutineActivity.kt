package com.yzx.demo.ui.coroutine

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.Observer
import com.yzx.demo.R
import com.yzx.demo.model.ZipData
import com.yzx.demo.mvp.BaseActivity
import com.yzx.demo.net.RetrofitHelper
import kotlinx.android.synthetic.main.activity_coroutines.*

/**
 * @author yzx
 * @date 2019/12/16
 * Description 协程
 */
class CoroutineActivity : BaseActivity<CoroutineViewModel>() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutines)

        viewModel.zipData.observe(this,
            Observer<ZipData> {
                result.text = "${it.ad}\n\n${it.configInfo}"
            })

        RetrofitHelper
            .getInstance().init("https://www.ihifly.com/")
        requestData.setOnClickListener {
            viewModel.getData()
        }
    }
}