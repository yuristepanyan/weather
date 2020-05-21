package com.example.weather.di

import android.content.Context
import com.bumptech.glide.Glide
import com.example.weather.BuildConfig
import com.example.weather.net.WeatherApi
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

val networkModule = module {
    single { gson() }

    single { interceptor() }

    single { httpClient(get(), cache(androidContext())) }

    single { retrofit(get(), get()) }

    single { weatherApi(get()) }

    single { glide(androidContext()) }
}

private fun gson(): Gson {
    return GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()
}

private fun interceptor(): HttpLoggingInterceptor {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    return interceptor
}

private fun cache(context: Context) = Cache(
    File(context.cacheDir, BuildConfig.CACHE_DIR),
    (10 * 1024 * 1024).toLong()
)

private fun httpClient(interceptor: HttpLoggingInterceptor, cache: Cache): OkHttpClient {
    val builder = OkHttpClient().newBuilder()
    builder.readTimeout(60, TimeUnit.SECONDS)
    builder.connectTimeout(30, TimeUnit.SECONDS)

    builder.addInterceptor { chain ->
        val requestBuilder = chain.request()
            .newBuilder()

        requestBuilder.addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")


        val request = requestBuilder.build()
        chain.proceed(request)
    }
        .cache(cache)

    if (BuildConfig.DEBUG) {
        builder.addInterceptor(interceptor)
    }

    return builder.build()
}

private fun retrofit(httpClient: OkHttpClient, gson: Gson): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
}

private fun weatherApi(retrofit: Retrofit) = retrofit.create(WeatherApi::class.java)

private fun glide(context: Context) = Glide.with(context)