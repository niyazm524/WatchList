package dev.procrastineyaz.watchlist.data.remote

import dev.procrastineyaz.watchlist.data.remote.interceptors.AuthInterceptor
import dev.procrastineyaz.watchlist.data.remote.interceptors.UnauthorizedInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val remoteAPIModule = module {
    factory<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(get<AuthInterceptor>())
            .addInterceptor(get<UnauthorizedInterceptor>())
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()
    }

    factory<GsonConverterFactory> { GsonConverterFactory.create() }

    factory { AuthInterceptor(get()) }
    single { UnauthorizedInterceptor(get(), androidApplication()) }

    single<Retrofit> {
        Retrofit.Builder()
//            .baseUrl("http://10.17.35.239:3000/api/")
            .baseUrl("http://192.168.0.28:3000/api/")
//            .baseUrl("https://watchlist.procrastineyaz.dev/api/")
            .client(get<OkHttpClient>())
            .addConverterFactory(get<GsonConverterFactory>())
            .build()
    }

    single<UsersAPIService> { get<Retrofit>().create(UsersAPIService::class.java) }
    single<ItemsAPIService> { get<Retrofit>().create(ItemsAPIService::class.java) }
    single<TrendsAPIService> { get<Retrofit>().create(TrendsAPIService::class.java) }
    single<SubscribersAPIService> { get<Retrofit>().create(SubscribersAPIService::class.java) }
    single<FeedAPIService> { get<Retrofit>().create(FeedAPIService::class.java) }
}
