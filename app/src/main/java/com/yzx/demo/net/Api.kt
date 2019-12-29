package com.yzx.demo.net

import com.yzx.demo.model.ConfigInfo
import com.yzx.demo.model.ScreenAd
import com.yzx.demo.mvp.BaseResult
import retrofit2.http.GET

/**
 * @author yzx
 * @date 2019/12/16
 * Description api
 */
interface Api {

    @GET("mall/advertis/getAdvertis")
    suspend fun getScreenAd(): BaseResult<ScreenAd>


    /**
     * 获取配置信息
     *
     * @return
     */
    @GET("mall/appConfig/getAppSingle")
    suspend fun getConfigInfo(): BaseResult<ConfigInfo>
}