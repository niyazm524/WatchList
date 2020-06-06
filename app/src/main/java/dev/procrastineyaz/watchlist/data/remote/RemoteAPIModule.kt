package dev.procrastineyaz.watchlist.data.remote

import dev.procrastineyaz.watchlist.data.remote.interceptors.AuthInterceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val remoteAPIModule = module {
    factory<OkHttpClient> {
        OkHttpClient.Builder()
            //.addInterceptor(get<AuthInterceptor>())
            //.addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()
    }

    factory<GsonConverterFactory> { GsonConverterFactory.create() }

    factory { AuthInterceptor() }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://watchlist.procrastineyaz.dev/api/")
            .client(get<OkHttpClient>())
            .addConverterFactory(get<GsonConverterFactory>())
            .build()
    }

    factory<UsersAPIService> { get<Retrofit>().create(UsersAPIService::class.java) }
}
