package com.example.practiceapplication.utils

import android.content.Context
import android.os.Build
import android.webkit.WebSettings
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.example.practiceapplication.MainActivity
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.InputStream

@GlideModule
class AppGlideModule : AppGlideModule() {

    lateinit var userAgentInterceptor: UserAgentInterceptor
    lateinit var okHttpClient: OkHttpClient

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)

        userAgentInterceptor = providesOkHttpUserAgentInterceptor(context)
        okHttpClient = providesOkHttpClient(userAgentInterceptor)

        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(okHttpClient)
        )

    }

    companion object {

        fun providesOkHttpClient(userAgentInterceptor: UserAgentInterceptor): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(userAgentInterceptor)
                .build()
        }

        fun providesOkHttpUserAgentInterceptor(context: Context): UserAgentInterceptor {
            return UserAgentInterceptor(context)
        }
    }
}

class UserAgentInterceptor(
    private val context: Context
) : Interceptor {

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val requestWithUserAgent: Request = originalRequest.newBuilder()
            .header("User-Agent", WebSettings.getDefaultUserAgent(context))
            .build()

        return chain.proceed(requestWithUserAgent)
    }
}