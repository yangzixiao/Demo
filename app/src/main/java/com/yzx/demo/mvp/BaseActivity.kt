package com.yzx.demo.mvp

import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.blankj.utilcode.util.ToastUtils
import java.lang.reflect.ParameterizedType

/**
 * @author yzx
 * @date 2019/12/23
 * Description Activity基类
 */
open class BaseActivity<VM : BaseViewModel> : AppCompatActivity(),
    DialogInterface.OnCancelListener {
    override fun onCancel(p0: DialogInterface?) {
        viewModel.cancelRequest()
    }

    private lateinit var loadingDialog: ProgressDialog
    lateinit var viewModel: VM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog = ProgressDialog(this)
        loadingDialog.setMessage("请稍后")
        loadingDialog.setCanceledOnTouchOutside(false)
        loadingDialog.setOnCancelListener(this)
        createViewModel()
        initLoadingAndMsg()
    }


    private fun initLoadingAndMsg() {
        viewModel.loadingState.observe(this, Observer<Boolean> {
            if (it) loadingDialog.show() else
                if (loadingDialog.isShowing) {
                    loadingDialog.hide()
                }
        })

        viewModel.toastBean.observe(this, Observer {
            val msg = it.msg
            if (TextUtils.isEmpty(msg)) {
                return@Observer
            }

            when (it.type) {
                BaseViewModel.TOASTTYPE.TOAST_TYPE_SUCCESS -> {
                    ToastUtils.setMsgColor(0xffffffff.toInt())
                    ToastUtils.setBgColor(0xff008577.toInt())
                }
                BaseViewModel.TOASTTYPE.TOAST_TYPE_FAIL -> {
                    ToastUtils.setMsgColor(0xffffffff.toInt())
                    ToastUtils.setBgColor(0xffff0000.toInt())
                }
                else -> {
                    ToastUtils.setMsgColor(0xffffffff.toInt())
                    ToastUtils.setBgColor(0x33000000)
                }
            }
            ToastUtils.showShort(msg as CharSequence)
        })
    }


    private fun createViewModel() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val tp = type.actualTypeArguments[0]
            val tClass = tp as? Class<VM> ?: BaseViewModel::class.java
            viewModel = ViewModelProviders.of(this).get(tClass) as VM
        }
    }

}