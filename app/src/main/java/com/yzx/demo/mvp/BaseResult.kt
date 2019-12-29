package com.yzx.demo.mvp

/**
 * @author yzx
 * @date 2019/12/23
 * Description
 */
data class BaseResult<out T>(
    val code: Int,
    val message: String,
    val data: T
)