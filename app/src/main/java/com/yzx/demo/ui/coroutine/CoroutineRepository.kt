package com.yzx.demo.ui.coroutine

import com.yzx.demo.net.Api
import com.yzx.demo.net.RetrofitHelper

/**
 * @author yzx
 * @date 2019/12/17
 * Description
 */
class CoroutineRepository {
    suspend fun getAd(){
        RetrofitHelper.getInstance().retrofit
            .create(Api::class.java)
            .getScreenAd()
    }
}