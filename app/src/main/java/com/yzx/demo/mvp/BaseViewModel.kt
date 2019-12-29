package com.yzx.demo.mvp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.StringUtils.getString
import com.google.gson.JsonParseException
import com.yzx.demo.R
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import org.json.JSONException
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException
import java.text.ParseException

/**
 * @author yzx
 * @date 2019/12/23
 * Description
 */
open class BaseViewModel : ViewModel() {

    protected var job: Job? = null
    var toastBean: MutableLiveData<ToastBean> = MutableLiveData()
    var loadingState: MutableLiveData<Boolean> = MutableLiveData()

    fun showLoading() {
        loadingState.postValue(true)
    }

    fun hideLoading() {
        loadingState.postValue(false)
    }

    fun showToast(msg: String) {
        showToast(msg, TOASTTYPE.TOAST_TYPE_DEFAULT)
    }

    fun showToast(msg: String, type: TOASTTYPE) {
        toastBean.postValue(ToastBean(msg, type))
    }

    fun cancelRequest() {
        job?.cancel()
    }

    fun isAllRequestSuccess(responses: ArrayList<BaseResult<Any>>): Boolean {

        responses.forEach {
            if ((it.code != 200) && (it.code != 1)) {
                return false
            }
        }
        return true
    }

    fun getErrorMsg(responses: ArrayList<BaseResult<Any>>): String {
        responses.forEach {
            if ((it.code != 200) && (it.code != 1)) {
                return it.message
            }
        }
        return ""
    }

    fun <M> dealResponse(result: BaseResult<M>) {
        if (result.code == 200) {
            onSuccess(result.data)
        } else {
            dealFail(result.message)
        }
    }

    open fun <M> onSuccess(response: M) {
        hideLoading()
    }

    open fun dealFail(msg: String) {
        showToast(msg, TOASTTYPE.TOAST_TYPE_FAIL)
        hideLoading()
    }

    fun dealException(e: Exception) {
        //协程取消报错
        if (e is CancellationException) {
            return
        }
        Log.e("ygl", e.toString())
        showToast(getErrorMsgByExceptionType(e), TOASTTYPE.TOAST_TYPE_FAIL)
        hideLoading()
    }

    private fun getErrorMsgByExceptionType(e: Exception): String {
        return when (e) {
            is HttpException -> "${e.code()}${getString(R.string.wlyc)}"
            is ConnectException, is UnknownHostException -> getString(R.string.ljcw)
            is InterruptedIOException -> getString(R.string.ljcs)
            is JsonParseException, is JSONException, is ParseException -> getString(R.string.jscw)
            else -> getString(R.string.wzcw)
        }
    }

    open inner class ToastBean(
        var msg: String = "",
        var type: TOASTTYPE = TOASTTYPE.TOAST_TYPE_DEFAULT
    )  

    enum class TOASTTYPE {
        TOAST_TYPE_DEFAULT,
        TOAST_TYPE_SUCCESS,
        TOAST_TYPE_FAIL,
        TOAST_TYPE_NOTICE,
    }

    enum class STATUS {
        SUCESS,
        FAIL,
        EMPTY,
        LOADING
    }
}