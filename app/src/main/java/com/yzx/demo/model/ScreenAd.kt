package com.yzx.demo.model

/**
 * @author yzx
 * @date 2019/12/23
 * Description
 */
data class ScreenAd(
    val homePageList: List<HomePage>
)

data class HomePage(
    val advertisImage: String,
    val advertisName: String,
    val advertisType: String,
    val advertisUrl: Any,
    val id: Int,
    val mold: Int
)