package com.pools.store.base

open class BaseResponse<T>(var data: T? = null) {
    var code: String? = ""
    var message: String? = ""
}

class BaseResponseNoData {
    var statusCode: Int? = -1
    var message: String? = ""
}


