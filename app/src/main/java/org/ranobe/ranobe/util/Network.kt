package org.ranobe.ranobe.util

import android.annotation.SuppressLint
import android.util.Log
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CompletionHandler
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.Request
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@SuppressLint("CustomX509TrustManager")
fun OkHttpClient.Builder.bypassSSLErrors() = also { builder ->
    runCatching {
        val trustAllCerts = object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) = Unit

            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) = Unit

            override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
        }
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf(trustAllCerts), SecureRandom())
        val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
        builder.sslSocketFactory(sslSocketFactory, trustAllCerts)
        builder.hostnameVerifier { _, _ -> true }
    }.onFailure {
        Log.e(this.toString(), "bypassSSLErrors: ",it )
    }
}

suspend fun Call.await(): Response = suspendCancellableCoroutine { continuation ->
    val callback = ContinuationCallCallback(this, continuation)
    enqueue(callback)
    continuation.invokeOnCancellation(callback)
}

fun postRequest(url: String, formBody: FormBody): Request {
    return Request.Builder().url(url).post(formBody).build()
}

fun getRequest(url: String): Request {
    return Request.Builder().url(url).build()
}

class CloudflareBypassInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        // Check if the response is a Cloudflare challenge (e.g., 503 status code)
        if (response.code == 503 && response.headers["Server"] == "cloudflare") {
            // Handle Cloudflare challenge here (e.g., solve JavaScript challenge)
            // This is a simplified example; actual implementation may require parsing JS challenges
            val bypassedRequest = request.newBuilder()
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .build()
            return chain.proceed(bypassedRequest)
        }

        return response
    }
}

internal class ContinuationCallCallback(
    private val call: Call,
    private val continuation: CancellableContinuation<Response>,
) : Callback, CompletionHandler {

    override fun onResponse(call: Call, response: Response) {
        if (continuation.isActive) {
            continuation.resume(response)
        }
    }

    override fun onFailure(call: Call, e: IOException) {
        if (!call.isCanceled() && continuation.isActive) {
            continuation.resumeWithException(e)
        }
    }

    override fun invoke(cause: Throwable?) {
        runCatching {
            call.cancel()
        }.onFailure { e ->
            cause?.addSuppressed(e)
        }
    }
}