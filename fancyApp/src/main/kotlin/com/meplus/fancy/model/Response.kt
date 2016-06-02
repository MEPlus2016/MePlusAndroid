package com.meplus.fancy.model

import android.content.Context
import cn.trinea.android.common.util.ToastUtils
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by dandanba on 4/11/16.
 */
data class Response<T>(
        @JsonProperty("Code")
        var Code: String ? = null,
        @JsonProperty("Message")
        var Message: String ? = null,
        @JsonProperty("Result")
        var Result: T ? = null
) {
    fun isOk(): Boolean {
        return "ok".equals(Code!!.toLowerCase());
    }

    fun showMessage(context: Context) {
        ToastUtils.show(context, Message);
    }
}