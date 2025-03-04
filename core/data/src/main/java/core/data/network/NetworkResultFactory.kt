package core.data.network

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Result
import javax.net.ssl.SSLPeerUnverifiedException
import kotlinx.serialization.Serializable
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import timber.log.Timber
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class NetworkResultFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) return null
        check(returnType is ParameterizedType) { "Return type must be a parameterized type." }

        val type = getParameterUpperBound(0, returnType)
        check(type is ParameterizedType) { "Return type must be a parameterized type." }
        if (getRawType(type) != Result::class.java) return null
        val responseType = getParameterUpperBound(0, type)

        val errorConverter = retrofit.responseBodyConverter<Result<Nothing, Failure>>(
            ErrorResponse::class.java,
            annotations
        )
        return ResultCallAdapter<Any>(responseType, errorConverter)
    }
}

@Serializable
class ErrorResponse(val status_code: Int, val status_message: String, val success: Boolean)

private class ResultCallAdapter<R>(
    private val type: Type,
    private val errorConverter: Converter<ResponseBody, Result<Nothing, Failure>>,
) : CallAdapter<R, Call<NetworkResult<R>>> {

    override fun responseType(): Type = type

    override fun adapt(call: Call<R>): Call<NetworkResult<R>> =
        ResultCall(call, type, errorConverter)
}

private class ResultCall<R>(
    private val delegate: Call<R>,
    private val successType: Type,
    private val errorConverter: Converter<ResponseBody, Result<Nothing, Failure>>,
) : Call<NetworkResult<R>> {

    override fun enqueue(callback: Callback<NetworkResult<R>>) {
        delegate.enqueue(object : Callback<R> {

            override fun onResponse(
                call: Call<R>,
                response: Response<R>
            ) {
                callback.onResponse(
                    this@ResultCall,
                    Response.success(response.toNetworkResult(successType, errorConverter))
                )
            }

            override fun onFailure(call: Call<R>, throwable: Throwable) {
                val error = if (throwable is SSLPeerUnverifiedException) {
                    Err(Failure("Ssl error"))
                } else {
                    Err(Failure("Unknown error"))
                }

                Timber.e(throwable)
                callback.onResponse(this@ResultCall, Response.success(error))
            }
        }
        )
    }

    override fun clone(): Call<NetworkResult<R>> =
        ResultCall(delegate.clone(), successType, errorConverter)

    override fun execute(): Response<NetworkResult<R>> = throw UnsupportedOperationException()

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun cancel() = delegate.cancel()

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}
