package com.meplus.fancy.model.entity

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

/**
 * Created by dandanba on 3/30/16.
 */
data class Book(
        @JsonProperty("SysNo") var SysNo: Int ? = null,
        @JsonProperty("Status") var Status: Int ? = null,
        @JsonProperty("Author") var Author: String ? = null,
        @JsonProperty("BookName") var BookName: String ? = null,
        @JsonProperty("BookISBN") var BookISBN: String ? = null,
        @JsonProperty("BookCoverId") var BookCoverId: String ? = null,
        @JsonProperty("ActionDate") var ActionDate: Date ? = null,
        @JsonProperty("PredictPay") var PredictPay: Int ? = null,
        @JsonProperty("ExpiredDay") var ExpiredDay: Int ? = null,
        @JsonProperty("ResultNo") var ResultNo: Int ? = null
)