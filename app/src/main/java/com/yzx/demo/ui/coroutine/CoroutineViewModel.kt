package com.yzx.demo.ui.coroutine

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yzx.demo.model.ZipData
import com.yzx.demo.mvp.BaseViewModel
import com.yzx.demo.net.Api
import com.yzx.demo.net.RetrofitHelper
import kotlinx.coroutines.launch

/**
 * @author yzx
 * @date 2019/12/16
 * Description
 */
class CoroutineViewModel : BaseViewModel() {

    private val api: Api by lazy {
        RetrofitHelper.getInstance().retrofit
            .create(Api::class.java)
    }
    var zipData: MutableLiveData<ZipData> = MutableLiveData()

    fun getData() {

        job = viewModelScope.launch {

            try {
                showLoading()
                val adResult = api.getScreenAd()
                val configResult = api.getConfigInfo()

                val responses = arrayListOf(adResult, configResult)

                if (isAllRequestSuccess(responses)) {
                    zipData.postValue(ZipData(adResult.data, configResult.data))
                    hideLoading()
                } else {
                    dealFail(getErrorMsg(responses))
                }
            } catch (e: Exception) {
                dealException(e)
            }
        }
    }

}