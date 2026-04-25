package com.twitter.twitterui.data.model

sealed class Status<T>(
    val statusCode: StatusCode = StatusCode.IDLE,
    val data: T? = null,
    val error: String? = null,
    val errorCode: Int? = null
) {

    class Success<T>(data: T?, error: String? = null) : Status<T>(StatusCode.SUCCESS, data, error)
    class Error<T>(data: T? = null, error: String?,errorCode: Int = 0) : Status<T>(StatusCode.ERROR, data, error,errorCode)

    class ServerError<T>(data: T? = null, error: String?) :
        Status<T>(StatusCode.SERVER_ERROR, data, error)

    class NoNetwork<T>(data: T? = null, error: String? = null) :
        Status<T>(StatusCode.NO_NETWORK, data, error)

    class NotAuthorized<T>(data: T? = null, error: String? = null) :
        Status<T>(StatusCode.NOT_AUTHORIZED, data, error)

    class Forbidden<T>(data: T? = null, error: String? = null) :
        Status<T>(StatusCode.FORBIDDEN, data, error)

    class NoData<T>(data: T? = null, error: String? = null) :
        Status<T>(StatusCode.NO_DATA, data, error)

    class OfflineData<T>(data: T? = null, error: String?) :
        Status<T>(StatusCode.OFFLINE_DATA, data, error)

    class Idle<T>(data: T? = null, error: String? = null) :
        Status<T>(StatusCode.IDLE, data, error)

    class CopyStatus<T, R>(status: Status<T>, newData: R?) :
        Status<R>(status.statusCode, newData, status.error)


    fun isSuccess(): Boolean {
        return statusCode == StatusCode.SUCCESS
    }

    fun isError(): Boolean {
        return statusCode == StatusCode.ERROR
    }

    fun isServerError(): Boolean {
        return statusCode == StatusCode.SERVER_ERROR
    }

    fun isOfflineData(): Boolean {
        return statusCode == StatusCode.OFFLINE_DATA
    }

    fun isNoNetwork(): Boolean {
        return statusCode == StatusCode.NO_NETWORK
    }

    fun isNoData(): Boolean {
        return statusCode == StatusCode.NO_DATA
    }

    fun isValid(): Boolean {
        return statusCode == StatusCode.VALID
    }

    fun isIdle(): Boolean {
        return statusCode == StatusCode.IDLE
    }

    fun isNotAuthorized(): Boolean {
        return statusCode == StatusCode.NOT_AUTHORIZED
    }

    fun isForbidden(): Boolean {
        return statusCode == StatusCode.FORBIDDEN
    }

    override fun toString(): String {
        return "Status(statusCode=$statusCode, data=$data, error=$error, errorCode=$errorCode)"
    }


}
