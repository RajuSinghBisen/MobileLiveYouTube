package com.example.mobileliveyoutube

import com.squareup.moshi.JsonDataException
import org.json.JSONObject
import retrofit2.Response
import java.io.EOFException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import kotlin.coroutines.Continuation
import kotlin.coroutines.suspendCoroutine


/** Defines all the related exceptions on how the repository manages data. */
sealed class RepositoryException(
    message: String,
    cause: Throwable? = null
): Throwable(message, cause)

/** Defines all the related exceptions on how the API manages requests and responses. */
sealed class ApiCallException(
    val errorCode: Int,
    errorMessage: String,
    apiError: ApiError? = null
): RepositoryException(apiError?.msg ?:"", null)

/** This exception is thrown whenever the API returns response with an error code 4XX.
 * Therefore, it must be handled by the application and, probably, re-submit the request after
 * fixing the related data.
 */
class ApiCallAppException(
    errorCode: Int,
    errorMessage: String,
    apiError: ApiError? = null
): ApiCallException(errorCode, errorMessage, apiError)

/** This exception is thrown whenever an API call fails due to authentication or authorization.
 * Application must logout account, allowing user to re-login using valid credentials.
 */
class ApiCallAuthException(
    errorCode: Int,
    errorMessage: String,
    apiError: ApiError? = null
): ApiCallException(errorCode, errorMessage, apiError)

/** This exception is thrown whenever the API returns response with an error code 5XX. */
class ApiCallServerException(
    errorCode: Int,
    errorMessage: String,
    apiError: ApiError? = null
): ApiCallException(errorCode, errorMessage, apiError)

/** This exception is thrown whenever the API returns response with an error code different than 4XX and 5XX. */
class ApiCallUnknownException(
    errorCode: Int,
    errorMessage: String,
    apiError: ApiError? = null
): ApiCallException(errorCode, errorMessage, apiError)

/** Defines all the related exceptions on how the API manages requests and responses. */
sealed class ApiConfigException(
    errorMessage: String,
    cause: Throwable?
): RepositoryException(errorMessage, cause) {
    init {
        if (cause!=null) {
            android.util.Log.e("ApiProcessing", "source::${cause.javaClass.name}: ${cause.message} -> $errorMessage")
        } else {
            android.util.Log.e("ApiProcessing", "source::Unknown: $errorMessage")
        }
    }
}

/** This exception is thrown whenever an API call fails due to an invalid specification.
 * This could be an invalid endpoint, invalid response error body, missing headers,
 * or any other non-compliant error within the API request or response.
 * These exceptions will require a change on the API or app design.
 */
class ApiConfigInvalidSpecificationException(
    message: String,
    cause: Throwable? = null
): ApiConfigException(message, cause)

/** This exception is thrown whenever an API call fails due to unavailability.
 */
class ApiConfigNotAvailableException(
    message: String,
    cause: Throwable
): ApiConfigException(message, cause)

/** This exception is thrown for any other underlying exceptions that are not
 * [ApiConfigInvalidSpecificationException] or [ApiConfigNotAvailableException]
 */
class ApiUnhandledException(
    message: String,
    cause: Throwable
): RepositoryException(message, cause) {
    init {
        android.util.Log.e("ApiProcessing", "Exception \"${cause.javaClass.name}\" must be added to \"ApiProcessing\" exception list")
    }
}

/** This class is used to wrap around the actual response error body as returned by the API.
 * As of now, such structure consists of just JSON Attribute `msg`.
 */
data class ApiError(
    val msg: String
)

/** Wrapper for API results. */
data class ApiResult<T>(
    val code: Int,
    val message: String,
    val data: T? = null
)
/** This method must be used for any API call.
 * @param ApiResponseModel Class that will be used when parsing response-body.
 * @param AppModel Class that will be used when returning result - wrapped within [ApiResult]
 * @param transform Transformation function to use to convert from [ApiResponseModel] to [AppModel]
 * @param enforceDataForSuccessWithContent If return code is 200 but response body is empty, throws an [ApiConfigInvalidSpecificationException] - default: true
 * @param responseProcessor If the caller requires to manipulate the actual response (eg. access headers). If this function returns true, that means that the
 *                          related coroutine has already being resumed. If it returns false, then processing will continue - default: null (no function)
 */
suspend fun <ApiResponseModel: Any, AppModel:Any> executeApiRequest(
    call: suspend () -> Response<ApiResponseModel>,
    /* @param input ApiResponseModel that has been automatically parsed by retrofit/moshi
     * @return AppModel that can be used within controller, viewmodel, and views.
     */
    transform: (input: ApiResponseModel) -> AppModel,
    enforceDataForSuccessWithContent: Boolean = true,
    responseProcessor: (
    /* @param res Actual retrofit response.
     * @param body Actual retrofit response-body.
     * @param coroutine Current coroutine continuation containing scope/context.
     * @return True, if function already resume coroutine. False, if coroutine has not been resumed.
     */
        (res: Response<ApiResponseModel>, body: ApiResponseModel?, coroutine: Continuation<ApiResult<AppModel>>) -> Boolean
    )? = null // only called if request was successful
): ApiResult<AppModel> {
    try {
        val response = call.invoke()
        return suspendCoroutine {
            if (response.isSuccessful) {
                with(response.code()) {
                    /* Evaluate body based on response code:
                     * - 200 : always means OK with content
                     * - 204 : always means OK with no-content
                     * - Other 2XX codes
                     */
                    when (this) {
                        // Specific cases where response-body is expected
                        in 200..201 -> {
                            val body = response.body()
                            val resumed = responseProcessor?.invoke(response, body, it)
                            if (resumed?.not() != false) {
                                if (body == null) {
                                    if (enforceDataForSuccessWithContent) {
                                        it.resumeWith(
                                            Result.failure(
                                                ApiConfigInvalidSpecificationException(
                                                    "source::API-endpoint: must return a response-body"
                                                )
                                            ))
                                    } else {
                                        // WARNING: 200 code means OK with content
                                        it.resumeWith(
                                            Result.success(
                                                ApiResult(
                                                    this,
                                                    response.message()
                                                )
                                            ))
                                    }
                                } else {
                                    it.resumeWith(
                                        Result.success(
                                            ApiResult(
                                                this,
                                                response.message(),
                                                transform(body)
                                            )
                                        ))
                                }
                            }
                        }
                        204 -> it.resumeWith(
                            Result.success(
                                ApiResult(
                                    this,
                                    response.message()
                                )
                            ))
                        // General case
                        in 200..299 -> {
                            val body = response.body()
                            if (responseProcessor!=null) {
                                // If responseProcessor returns true, then the coroutine has already being resumed.
                                // Otherwise, we need to keep processing and resume coroutine
                                if (!responseProcessor.invoke(response, body, it)) {
                                    if (body == null) {
                                        it.resumeWith(
                                            Result.success(
                                                ApiResult(
                                                    this,
                                                    response.message()
                                                )
                                            ))
                                    } else {
                                        it.resumeWith(
                                            Result.success(
                                                ApiResult(
                                                    this,
                                                    response.message(),
                                                    transform(body)
                                                )
                                            ))
                                    }
                                }
                            } else {
                                if (body == null) {
                                    it.resumeWith(
                                        Result.success(
                                            ApiResult(
                                                this,
                                                response.message()
                                            )
                                        ))
                                } else {
                                    it.resumeWith(
                                        Result.success(
                                            ApiResult(
                                                this,
                                                response.message(),
                                                transform(body)
                                            )
                                        ))
                                }
                            }
                        }
                        // TODO How to handle re-directions?
                    }
                }
            } else {
                // Since call has not been successful, there should be a response-body-error in the payload
                val error = response.errorBody()
                // Errors are wrapped within an ApiError
                val apiError =
                    if (error != null) {
                        try {
                            val jsonError = JSONObject(error.string())
                            // WARNING: This is specific for C&CC API as per specifications
                            ApiError(jsonError.getString("msg"))
                        } catch (e: Throwable) {
                            throw ApiConfigInvalidSpecificationException(
                                "source::${e.javaClass.simpleName}: ${e.message ?: "no-message"}",
                                e
                            )
                        }
                    } else {
                        null
                    }
                with(response.code()) {
                    when (this) {
                        // WARNING!! Throw unique exception - app must force user to re-login, as per C&CC API specifications
                        401         -> it.resumeWith(
                            Result.failure(
                                ApiCallAuthException(
                                    this,
                                    "Auth error: ${response.message()}",
                                    apiError
                                )
                            ))
                        // 4XX are related to user request errors - app should display error message on what is wrong.
                        in 400..499 -> it.resumeWith(
                            Result.failure(
                                ApiCallAppException(
                                    this,
                                    "Request error: ${response.message()}",
                                    apiError
                                )
                            ))
                        // 5XX are related to API request errors - nothing app can do - API may have a bug or has changed specifications without notifying app-developers
                        in 500..599 -> it.resumeWith(
                            Result.failure(
                                ApiCallServerException(
                                    this,
                                    "Server error: ${response.message()}",
                                    apiError
                                )
                            ))
                        // Unknown errors generated by API
                        else        -> it.resumeWith(
                            Result.failure(
                                ApiCallUnknownException(
                                    this,
                                    "Unknown failure: ${response.message()}",
                                    apiError
                                )
                            ))
                    }
                }
            }
        }
        /* NOTE:
         * The "catch" section below are exceptions triggered by retrofit, okhttp, moshi, or underlying
         * system libraries used to make the actual call.
         */
    } catch (e: EOFException) {
        // OkHttp will throw this exception if the response was expected to have a body but came empty
        throw ApiConfigInvalidSpecificationException(
            "source::${e.javaClass.simpleName}: ${e.message ?: "no-message"}", e
        )
    } catch (e: IllegalArgumentException) {
        // Thrown whenever a response-body does not comply with the AppModel (not null fields)
        throw ApiConfigInvalidSpecificationException(
            "source::${e.javaClass.simpleName}: ${e.message ?: "no-message"}", e
        )
    } catch (e: JsonDataException) {
        // Moshi will throw this exception whenever the response-body JSON model does not fit with the specified `data-class`
        // For example, a non-null attribute, a string coming as boolean, etc.
        throw ApiConfigInvalidSpecificationException(
            "source::${e.javaClass.simpleName}: ${e.message ?: "no-message"}", e
        )
    } catch (e: InterruptedIOException) {
        // OkHttp will throw this exception if the API didn't return a response within the timeout limit.
        // This could be solved by increasing the timeout value in the retrofit client - see Retrofit.callTimeout(timeout, Unit)
        throw ApiConfigNotAvailableException(
            "source::${e.javaClass.simpleName}: ${e.message ?: "no-message"}", e
        )
    } catch (e: ConnectException) {
        // If API endpoint cannot be contacted (e.g. app is airplane mode, no connection available, etc)
        throw ApiConfigNotAvailableException(
            "source::${e.javaClass.simpleName}: ${e.message ?: "no-message"}", e
        )
    } catch (e: SocketTimeoutException) {
        // Connection to API endpoint was successful, received request, but it is not responding
        throw ApiConfigNotAvailableException(
            "source::${e.javaClass.simpleName}: ${e.message ?: "no-message"}", e
        )
    } catch (e: RepositoryException) {
        e.printStackTrace()
        // Just forward since it is already handled
        throw e
    } catch (e: Throwable) {
        e.printStackTrace()
        throw ApiUnhandledException(
            "source::${e.javaClass.simpleName}: ${e.message ?: "no-message"}", e
        )
    }
}

